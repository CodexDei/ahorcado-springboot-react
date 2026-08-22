package com.codexdei.crudjuegoahorcado.ahorcado.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.GameSessionDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.request.GuessRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.GameWordResponseDto;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.ScoreBoard;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.User;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;
import com.codexdei.crudjuegoahorcado.ahorcado.repositories.ScoreBoardRepository;

@Service
public class GameServiceImpl implements GameService {

        private final WordService wordService;

        private final ScoreBoardRepository scoreBoardRepository;

        private final Map<UUID, GameSessionDto> sessions = new ConcurrentHashMap<>();

        public GameServiceImpl(

                        WordService wordService,

                        ScoreBoardRepository scoreBoardRepository) {

                this.wordService = wordService;
                this.scoreBoardRepository = scoreBoardRepository;
        }

        @Override
        public GameWordResponseDto startGame(
                        Long userId) {

                Difficulty difficulty = Difficulty.EASY;

                Word word = wordService.getRandomWord(
                                difficulty);

                GameSessionDto session = new GameSessionDto();

                session.setId(
                                UUID.randomUUID());

                session.setUserId(
                                userId);

                session.setCurrentDifficulty(
                                difficulty);

                session.setOriginalWord(
                                word.getWord());

                session.setMaskedWord(

                                "*".repeat(
                                                word.getWord().length()).toCharArray());

                session.setAttemptsLeft(

                                calculateAttempts(
                                                word.getWord(),
                                                difficulty));

                session.setStartTime(
                                LocalDateTime.now());

                sessions.put(
                                session.getId(),
                                session);

                return buildResponse(
                                session,
                                false,
                                null);
        }

        @Override
        public GameWordResponseDto guessLetter(
                        UUID gameId,
                        GuessRequestDto request) {

                GameSessionDto session = sessions.get(gameId);

                if (session == null) {

                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "La partida no existe");
                }

                if (isTimeExpired(session)) {

                        saveScore(session, false);

                        sessions.remove(gameId);

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Tiempo agotado");
                }

                char guessedLetter = request
                                .getLetter()
                                .toLowerCase()
                                .charAt(0);

                char[] maskedWord = session.getMaskedWord();

                String originalWord = session
                                .getOriginalWord()
                                .toLowerCase();

                boolean found = false;

                for (int i = 0; i < originalWord.length(); i++) {

                        if (originalWord.charAt(i) == guessedLetter) {

                                maskedWord[i] = guessedLetter;

                                found = true;
                        }
                }

                if (!found) {

                        session.setAttemptsLeft(

                                        session.getAttemptsLeft() - 1);
                }

                /*
                 * Perdio
                 */

                if (session.getAttemptsLeft() <= 0) {

                        saveScore(session, false);

                        sessions.remove(gameId);

                        return buildResponse(

                                        session,
                                        true,
                                        "Has perdido");
                }

                /*
                 * Completo la palabra
                 */

                if (new String(maskedWord).equals(originalWord)) {

                        session.setTotalScore(

                                        session.getTotalScore()
                                                        + calculateScore(session));

                        Difficulty nextDifficulty = getNextDifficulty(

                                        session.getCurrentDifficulty());

                        /*
                         * Gano el juego
                         */

                        if (nextDifficulty == null) {

                                saveScore(session, true);

                                sessions.remove(gameId);

                                return buildResponse(

                                                session,
                                                true,
                                                "Has ganado el juego");
                        }

                        /*
                         * Pasar al siguiente nivel
                         */

                        loadNextLevel(

                                        session,
                                        nextDifficulty);

                        return buildResponse(

                                        session,
                                        false,
                                        "Nivel superado");
                }

                return buildResponse(

                                session,
                                false,
                                null);
        }

        private Long calculateRemainingTime(
                        GameSessionDto session) {

                long elapsedSeconds = Duration.between(
                                session.getStartTime(),
                                LocalDateTime.now()).getSeconds();

                long remainingTime = getTimeLimit(
                                session.getCurrentDifficulty()) - elapsedSeconds;

                return Math.max(remainingTime, 0);
        }

        private boolean isTimeExpired(
                        GameSessionDto session) {

                long elapsedSeconds = Duration.between(

                                session.getStartTime(),
                                LocalDateTime.now()

                ).getSeconds();

                return elapsedSeconds >= getTimeLimit(

                                session.getCurrentDifficulty());
        }

        private Long getTimeLimit(
                        Difficulty difficulty) {

                return switch (difficulty) {

                        case EASY -> 180L;

                        case MEDIUM -> 120L;

                        case HARD -> 60L;
                };
        }

        private Difficulty getNextDifficulty(
                        Difficulty currentDifficulty) {

                return switch (currentDifficulty) {

                        case EASY -> Difficulty.MEDIUM;

                        case MEDIUM -> Difficulty.HARD;

                        case HARD -> null;
                };
        }

        private void loadNextLevel(

                        GameSessionDto session,

                        Difficulty nextDifficulty) {

                Word nextWord = wordService.getRandomWord(
                                nextDifficulty);

                session.setCurrentDifficulty(
                                nextDifficulty);

                session.setOriginalWord(
                                nextWord.getWord());

                session.setMaskedWord(

                                "_".repeat(
                                                nextWord.getWord().length()).toCharArray());

                session.setAttemptsLeft(

                                calculateAttempts(
                                                nextWord.getWord(),
                                                nextDifficulty));

                session.setStartTime(
                                LocalDateTime.now());
        }

        private Long calculateScore(
                        GameSessionDto session) {

                return switch (session.getCurrentDifficulty()) {

                        case EASY ->
                                session.getAttemptsLeft() * 100L;

                        case MEDIUM ->
                                session.getAttemptsLeft() * 200L;

                        case HARD ->
                                session.getAttemptsLeft() * 300L;
                };
        }

        private void saveScore(

                        GameSessionDto session,

                        boolean won) {

                if (

                !won

                                && session.getCurrentDifficulty() == Difficulty.EASY

                ) {

                        return;
                }

                ScoreBoard scoreBoard = new ScoreBoard();

                User user = new User();

                user.setId(
                                session.getUserId());

                scoreBoard.setUser(
                                user);

                scoreBoard.setScore(
                                session.getTotalScore());

                scoreBoard.setMaxLevel(
                                session.getCurrentDifficulty());

                scoreBoard.setDate(
                                LocalDateTime.now());

                scoreBoardRepository.save(
                                scoreBoard);
        }

        private GameWordResponseDto buildResponse(
                        GameSessionDto session,
                        boolean finished,
                        String message) {

                GameWordResponseDto response = new GameWordResponseDto();

                response.setGameId(session.getId());

                response.setMaskedWord(
                                new String(session.getMaskedWord()));

                response.setDifficulty(
                                session.getCurrentDifficulty());

                response.setAttemptsLeft(
                                session.getAttemptsLeft());

                response.setTotalScore(
                                session.getTotalScore());

                response.setTimeLeft(
                                calculateRemainingTime(session));

                response.setFinished(
                                finished);

                response.setMessage(
                                message);

                return response;
        }

        private Integer calculateAttempts(
                        String word,
                        Difficulty difficulty) {

                return switch (difficulty) {

                        case EASY -> Math.max(
                                        word.length() * 2,
                                        8);

                        case MEDIUM -> Math.max(
                                        word.length(),
                                        6);

                        case HARD -> Math.max(
                                        word.length() / 2,
                                        4);
                };
        }

}
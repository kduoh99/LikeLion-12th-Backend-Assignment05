package org.likelion.likelionassignmentcrud.game.application;

import org.likelion.likelionassignmentcrud.developer.domain.Developer;
import org.likelion.likelionassignmentcrud.developer.domain.repository.DeveloperRepository;
import org.likelion.likelionassignmentcrud.game.api.dto.request.GameSaveReqDto;
import org.likelion.likelionassignmentcrud.game.api.dto.response.GameInfoResDto;
import org.likelion.likelionassignmentcrud.game.api.dto.response.GameListResDto;
import org.likelion.likelionassignmentcrud.game.domain.Game;
import org.likelion.likelionassignmentcrud.game.domain.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;

    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
    }

    // Create
    @Transactional
    public void gameSave(GameSaveReqDto gameSaveReqDto) {
        Developer developer = developerRepository.findById(gameSaveReqDto.developerId())
                .orElseThrow(IllegalArgumentException::new);

        Game game = Game.builder()
                .name(gameSaveReqDto.name())
                .genre(gameSaveReqDto.genre())
                .platform(gameSaveReqDto.platform())
                .developer(developer)
                .build();

        gameRepository.save(game);
    }

    // 개발사에 따른 게임 조회
    public GameListResDto gameFindDeveloper(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(null);

        List<Game> games = gameRepository.findByDeveloper(developer);
        List<GameInfoResDto> gameInfoResDtoList = games.stream()
                .map(GameInfoResDto::from)
                .toList();

        return GameListResDto.from(gameInfoResDtoList);
    }
}

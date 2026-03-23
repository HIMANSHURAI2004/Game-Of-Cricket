package com.cricket.GameOfCricket.services;

import com.cricket.GameOfCricket.common.constants.CricketConstants;
import com.cricket.GameOfCricket.common.exception.InningsNotFoundException;
import com.cricket.GameOfCricket.common.exception.InvalidMatchRequestException;
import com.cricket.GameOfCricket.common.exception.MatchNotFoundException;
import com.cricket.GameOfCricket.common.exception.TeamNotFoundException;
import com.cricket.GameOfCricket.engine.InningsResult;
import com.cricket.GameOfCricket.engine.InningsSimulator;
import com.cricket.GameOfCricket.engine.TossEngine;
import com.cricket.GameOfCricket.model.dto.request.StartMatchRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.*;
import com.cricket.GameOfCricket.model.entity.*;
import com.cricket.GameOfCricket.model.enums.MatchStatus;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import com.cricket.GameOfCricket.model.enums.TossDecision;
import com.cricket.GameOfCricket.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService{
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final InningsRepository inningsRepository;
    private final OverRepository overRepository;
    private final TossEngine tossEngine;
    private final InningsSimulator inningsSimulator;

    public MatchResponseDTO simulateMatch(StartMatchRequestDTO request){
        if(request.getTeamAId().equals(request.getTeamBId())){
            throw new InvalidMatchRequestException("Team A and Team B cannot be the same");
        }

        Team teamA = teamRepository.findById(request.getTeamAId())
                .orElseThrow(() -> new TeamNotFoundException(request.getTeamAId()));

        Team teamB = teamRepository.findById(request.getTeamBId())
                .orElseThrow(() -> new TeamNotFoundException(request.getTeamBId()));

        List<Player> teamASquad = fetchPlayersInOrder(teamA);
        List<Player> teamBSquad = fetchPlayersInOrder(teamB);
        List<Player> teamABowlers = filterBowlers(teamASquad);
        List<Player> teamBBowlers = filterBowlers(teamBSquad);

        int maxOvers = request.getMaxOvers();

        Toss toss = tossEngine.conductToss(teamA.getName(), teamB.getName());

        Team battingFirstTeam;
        Team bowlingFirstTeam;
        List<Player> battingFirstSquad;
        List<Player> bowlingFirstSquad;
        List<Player> battingFirstBowlers;
        List<Player> bowlingFirstBowlers;

        if(toss.getWinner().equals(teamA.getName())){
            if(toss.getDecision() == TossDecision.BAT){
                battingFirstTeam = teamA;
                bowlingFirstTeam = teamB;
                battingFirstSquad = teamASquad;
                bowlingFirstSquad = teamBSquad;
                battingFirstBowlers = teamABowlers;
                bowlingFirstBowlers = teamBBowlers;
            }
            else{
                battingFirstTeam = teamB;
                bowlingFirstTeam = teamA;
                battingFirstSquad = teamBSquad;
                bowlingFirstSquad = teamASquad;
                battingFirstBowlers = teamBBowlers;
                bowlingFirstBowlers = teamABowlers;
            }
        }
        else {
            if(toss.getDecision() == TossDecision.BAT){
                battingFirstTeam = teamB;
                bowlingFirstTeam = teamA;
                battingFirstSquad = teamBSquad;
                bowlingFirstSquad = teamASquad;
                battingFirstBowlers = teamBBowlers;
                bowlingFirstBowlers = teamABowlers;
            }
            else{
                battingFirstTeam = teamA;
                bowlingFirstTeam = teamB;
                battingFirstSquad = teamASquad;
                bowlingFirstSquad = teamBSquad;
                battingFirstBowlers = teamABowlers;
                bowlingFirstBowlers = teamBBowlers;
            }
        }

        InningsResult firstInningsResult = inningsSimulator.simulateFullInnings(
                battingFirstSquad,
                bowlingFirstBowlers,
                battingFirstTeam.getId(),
                bowlingFirstTeam.getId(),
                maxOvers,
                -1);

        Innings firstInnings = inningsRepository.save(firstInningsResult.getInnings());
        saveOvers(firstInningsResult.getOvers(), firstInnings.getId());

        int target = firstInnings.getTotalRuns() +1;

        InningsResult secondInningsResult = inningsSimulator.simulateFullInnings(
                bowlingFirstSquad,
                battingFirstBowlers,
                bowlingFirstTeam.getId(),
                battingFirstTeam.getId(),
                maxOvers,
                target
        );

        Innings secondInnings = inningsRepository.save(secondInningsResult.getInnings());
        saveOvers(secondInningsResult.getOvers(), secondInnings.getId());

        MatchResult result = determineResult(firstInnings, secondInnings , battingFirstTeam.getName() , bowlingFirstTeam.getName(),maxOvers);

        Match match = Match.builder()
                .teamAId(teamA.getId())
                .teamBId(teamB.getId())
                .toss(toss)
                .maxOvers(maxOvers)
                .firstInningsId(firstInnings.getId())
                .secondInningsId(secondInnings.getId())
                .result(result)
                .status(MatchStatus.COMPLETED)
                .build();

        match = matchRepository.save(match);

        return toMatchResponse(match, teamA, teamB, firstInnings, secondInnings);


    }

    @Override
    public MatchResponseDTO getMatchById(String id){
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        Team teamA = teamRepository.findById(match.getTeamAId())
                .orElseThrow(() -> new TeamNotFoundException(match.getTeamAId()));

        Team teamB = teamRepository.findById(match.getTeamBId())
                .orElseThrow(() -> new TeamNotFoundException(match.getTeamBId()));

        Innings firstInnings = inningsRepository.findById(match.getFirstInningsId())
                .orElseThrow(() -> new InningsNotFoundException(match.getFirstInningsId()));

        Innings secondInnings = inningsRepository.findById(match.getSecondInningsId())
                .orElseThrow(() -> new InningsNotFoundException(match.getSecondInningsId()));

        return toMatchResponse(match, teamA, teamB, firstInnings, secondInnings);
    }

    @Override
    public ScoreBoardResponseDTO getScorecard(String id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        Team teamA = teamRepository.findById(match.getTeamAId())
                .orElseThrow(() -> new TeamNotFoundException(match.getTeamAId()));
        Team teamB = teamRepository.findById(match.getTeamBId())
                .orElseThrow(() -> new TeamNotFoundException(match.getTeamBId()));

        Innings first = inningsRepository.findById(match.getFirstInningsId())
                .orElseThrow(() -> new InningsNotFoundException(match.getFirstInningsId()));
        Innings second = inningsRepository.findById(match.getSecondInningsId())
                .orElseThrow(() -> new InningsNotFoundException(match.getSecondInningsId()));

        return ScoreBoardResponseDTO.builder()
                .matchId(id)
                .teamA(teamA.getName())
                .teamB(teamB.getName())
                .overs(match.getMaxOvers())
                .toss(toTossResponse(match.getToss()))
                .firstInnings(toInningsScoreCard(first))
                .secondInnings(toInningsScoreCard(second))
                .result(toMatchResultResponse(match.getResult()))
                .build();
    }

    @Override
    public InningsDetailResponseDTO getInnings(String matchId, int inningsNum){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        if(inningsNum!=1 && inningsNum!=2){
            throw new InvalidMatchRequestException("Innings number should be 1 or 2");
        }

        Innings innings = inningsRepository.findById(inningsNum == 1 ? match.getFirstInningsId() : match.getSecondInningsId())
                .orElseThrow(() -> new InningsNotFoundException(inningsNum == 1 ? match.getFirstInningsId() : match.getSecondInningsId()));

        List<Over> overs = overRepository
                .findByInningsIdOrderByOverNumber(innings.getId());

        List<OverResponseDTO> overResponse = overs.stream()
                .map(this::toOverResponse)
                .toList();

        return InningsDetailResponseDTO.builder()
                .battingTeam(innings.getBattingTeamId())
                .bowlingTeam(innings.getBowlingTeamId())
                .oversPlayed(innings.getLegalBallsBowled()/6)
                .maxOvers(match.getMaxOvers())
                .totalRuns(innings.getTotalRuns())
                .totalWickets(innings.getTotalWickets())
                .overs(overResponse)
                .battingCard(innings.getBattingCards())
                .bowlingCard(innings.getBowlingCards())
                .fallOfWickets(innings.getFallOfWickets())
                .totalExtras(innings.getTotalExtras())
                .targetScore(innings.getTargetScore())
                .isComplete(innings.isComplete())
                .build();
    }

    @Override
    public MatchResultResponseDTO getResult(String id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        if (match.getResult() == null) {
            throw new InvalidMatchRequestException(
                    "Match not completed yet. Status: " + match.getStatus());
        }

        return toMatchResultResponse(match.getResult());
    }

    private void saveOvers(List<Over> overs, String inningsId) {
        for (Over over : overs) {
            over.setInningsId(inningsId);
            overRepository.save(over);
        }
    }
    private List<Player> fetchPlayersInOrder(Team team) {
        List<Player> allPlayers = playerRepository
                .findAllById(team.getPlayerIds());

        List<Player> ordered = new ArrayList<>();
        for (String playerId : team.getPlayerIds()) {
            allPlayers.stream()
                    .filter(p -> p.getId().equals(playerId))
                    .findFirst()
                    .ifPresent(ordered::add);
        }
        return ordered;
    }

    private List<Player> filterBowlers(List<Player> players) {
        return players.stream()
                .filter(p -> p.getRole() == PlayerRole.BOWLER
                        || p.getRole() == PlayerRole.ALL_ROUNDER)
                .collect(Collectors.toList());
    }

    private MatchResult determineResult(Innings firstInnings,
                                        Innings secondInnings,
                                        String battingFirstName,
                                        String bowlingFirstName,
                                        int maxOvers) {
        int firstInningsRuns = firstInnings.getTotalRuns();
        int secondInningsRuns = secondInnings.getTotalRuns();

        int target = firstInningsRuns + 1;

        if (secondInningsRuns >= target) {
            int wicketsRemaining = CricketConstants.MAX_WICKETS
                    - secondInnings.getTotalWickets();
            int ballsRemaining = (maxOvers * CricketConstants.BALLS_PER_OVER)
                    - secondInnings.getLegalBallsBowled();
            String oversRemaining = ballsRemaining / 6 + "." + ballsRemaining % 6;

            return MatchResult.builder()
                    .winner(bowlingFirstName)
                    .isTie(false)
                    .winMarginWickets(wicketsRemaining)
                    .winMarginRuns(0)
                    .summary(bowlingFirstName + " won by "
                            + wicketsRemaining + " wicket"
                            + (wicketsRemaining == 1 ? "" : "s")
                            + " (with " + oversRemaining + " overs remaining)")
                    .build();

        } else if (firstInningsRuns > secondInningsRuns) {
            int margin = firstInningsRuns - secondInningsRuns;
            return MatchResult.builder()
                    .winner(battingFirstName)
                    .isTie(false)
                    .winMarginRuns(margin)
                    .winMarginWickets(0)
                    .summary(battingFirstName + " won by "
                            + margin + " run" + (margin == 1 ? "" : "s"))
                    .build();

        } else {
            return MatchResult.builder()
                    .isTie(true)
                    .winMarginRuns(0)
                    .winMarginWickets(0)
                    .summary("Match tied!")
                    .build();
        }

    }

    private MatchResponseDTO toMatchResponse(Match match, Team teamA, Team teamB, Innings firstInnings, Innings secondInnings) {
        return MatchResponseDTO.builder()
                .id(match.getId())
                .teamA(teamA)
                .teamB(teamB)
                .maxOvers(match.getMaxOvers())
                .toss(toTossResponse(match.getToss()))
                .status(match.getStatus())
                .firstInnings(toInningsScoreCard(firstInnings))
                .secondInnings(toInningsScoreCard(secondInnings))
                .result(toMatchResultResponse(match.getResult()))
                .build();
    }

    private TossResponseDTO toTossResponse(Toss toss) {
        if (toss == null) return null;
        return TossResponseDTO.builder()
                .winner(toss.getWinner())
                .decision(toss.getDecision())
                .build();
    }

    private MatchResultResponseDTO toMatchResultResponse(MatchResult result) {
        if (result == null) return null;
        return MatchResultResponseDTO.builder()
                .winner(result.getWinner())
                .isTie(result.isTie())
                .winMarginRuns(result.getWinMarginRuns())
                .winMarginWickets(result.getWinMarginWickets())
                .summary(result.getSummary())
                .build();
    }

    private InningsResponseDTO toInningsScoreCard(Innings innings) {

        return InningsResponseDTO.builder()
                .battingTeamId(innings.getBattingTeamId())
                .bowlingTeamId(innings.getBowlingTeamId())
                .inningsId(innings.getId())
                .targetScore(innings.getTargetScore())
                .totalRuns(innings.getTotalRuns())
                .totalWickets(innings.getTotalWickets())
                .totalExtras(innings.getTotalExtras())
                .legalBallsBowled(innings.getLegalBallsBowled())
                .battingCards(innings.getBattingCards())
                .bowlingCards(innings.getBowlingCards())
                .fallOfWickets(innings.getFallOfWickets())
                .isComplete(innings.isComplete())
                .build();
    }

    private OverResponseDTO toOverResponse(Over over) {
        return OverResponseDTO.builder()
                .id(over.getId())
                .overNumber(over.getOverNumber())
                .inningsId(over.getInningsId())
                .bowler(over.getBowler())
                .balls(over.getBalls())
                .runsInOver(over.getRunsInOver())
                .wicketsInOver(over.getWicketsInOver())
                .build();
    }


}

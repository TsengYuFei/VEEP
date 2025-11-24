package com.example.api.Service;


import com.example.api.DTO.Response.*;
import com.example.api.Entity.BoothLog;
import com.example.api.Entity.ExpoLog;
import com.example.api.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final ExpoLogService expoLogService;
    private final BoothLogService boothLogService;
    private final ExpoHelperService expoHelperService;
    private final ContentLogService contentLogService;


    private long calculateDurationSeconds(LocalDateTime enterAt, LocalDateTime exitAt) {
        LocalDateTime endTime = (exitAt != null)
                ? exitAt
                : enterAt.toLocalDate().plusDays(1).atStartOfDay().minusNanos(1);

        return Duration.between(enterAt, endTime).getSeconds();
    }

    public DataResponse expoDataAnalysis(Integer expoID){
        System.out.println("DataService: expoDataAnalysis>> "+expoID);
        LocalDate today = LocalDate.now();
        int totalPeople = 0;
        long totalTime = 0;
        int totalBooth = 0;
        int usedAI = 0;

        DataOverviewResponse overview = new DataOverviewResponse();
        List<DailyIncreaseResponse> dailyVisitor = new ArrayList<>();
        List<DailyStayAverageResponse> dailyStayAvg = new ArrayList<>();
        long totalAvg = 0;
        DataRateResponse rate = new DataRateResponse();

        overview.setTotalVisitors(expoLogService.getTotalNumberByExpoID(expoID));
        for(int i=6; i>=0; i--){
            LocalDate date = today.minusDays(i);
            Integer dailyPeople = expoLogService.getPeopleNumByExpoIDAndDate(expoID, date);
            totalPeople+=dailyPeople;

            DailyIncreaseResponse visitor = new DailyIncreaseResponse();
            visitor.setDate(date);
            visitor.setCount(dailyPeople);

            dailyVisitor.add(visitor);



            List<ExpoLog> logs = expoLogService.getExpoLogByExpoIDAndDate(expoID, date);
            long dailyTime = 0;

            for(ExpoLog log : logs){
                long seconds = calculateDurationSeconds(log.getEnterAt(), log.getExitAt());
                dailyTime+=seconds;

                if(log.getHasUsedAi()) usedAI++;
            }

            DailyStayAverageResponse avgStay = new DailyStayAverageResponse();
            avgStay.setDate(date);
            if(dailyPeople == 0) avgStay.setAvgDurationSeconds(0);
            else avgStay.setAvgDurationSeconds(dailyTime/(long)dailyPeople);
            totalTime+=dailyTime;

            dailyStayAvg.add(avgStay);


            List<User> users = expoLogService.getUserByExpoIDAndAccountAndDate(expoID, date);
            for(User user : users) totalBooth += boothLogService.getBoothNumberByExpoIDAndAccount(expoID, user.getUserAccount(), date);

        }
        overview.setNewVisitorsLast7Days(totalPeople);


        int boothNumber = expoHelperService.getBoothNumber(expoID);
        if(totalPeople == 0){
            rate.setParticipationRate(0.0);
            rate.setAiUsageRate(0.0);
        }else if(totalBooth == 0) rate.setParticipationRate(0.0);
        else {
            totalAvg = totalTime/totalPeople;

            double averageBoothVisitPerPerson = (double) totalBooth / totalPeople;
            double pr = averageBoothVisitPerPerson / boothNumber;
            pr = Math.round(pr * 10.0) / 10.0;

            double ar = (double) usedAI / totalPeople;
            ar = Math.round(ar * 10.0) / 10.0;

            rate.setParticipationRate(pr);
            rate.setAiUsageRate(ar);
        }


        DataResponse response = new DataResponse();
        response.setOverview(overview);
        response.setDailyVisitor(dailyVisitor);
        response.setDailyStayAverage(dailyStayAvg);
        response.setTotalAvgDurationSeconds(totalAvg);
        response.setEngagement(rate);
        return response;
    }

    public DataResponse boothDataAnalysis(Integer boothID){
        System.out.println("DataService: boothDataAnalysis>> "+boothID);
        LocalDate today = LocalDate.now();
        int totalPeople = 0;
        long totalTime = 0;
        int totalContent = 0;
        int usedAI = 0;

        DataOverviewResponse overview = new DataOverviewResponse();
        List<DailyIncreaseResponse> dailyVisitor = new ArrayList<>();
        List<DailyStayAverageResponse> dailyStayAvg = new ArrayList<>();
        long totalAvg = 0;
        DataRateResponse rate = new DataRateResponse();

        overview.setTotalVisitors(boothLogService.getTotalNumberByBoothID(boothID));
        for(int i=6; i>=0; i--){
            LocalDate date = today.minusDays(i);
            Integer dailyPeople = boothLogService.getPeopleNumByBoothIDAndDate(boothID, date);
            totalPeople+=dailyPeople;

            DailyIncreaseResponse visitor = new DailyIncreaseResponse();
            visitor.setDate(date);
            visitor.setCount(dailyPeople);

            dailyVisitor.add(visitor);


            List<BoothLog> logs = boothLogService.getBoothLogByBoothIDAndDate(boothID, date);
            long dailyTime = 0;

            for(BoothLog log : logs){
                long seconds = calculateDurationSeconds(log.getEnterAt(), log.getExitAt());
                dailyTime+=seconds;

                if(log.getHasUsedAi()) usedAI++;
            }

            DailyStayAverageResponse avgStay = new DailyStayAverageResponse();
            avgStay.setDate(date);
            if(dailyPeople == 0) avgStay.setAvgDurationSeconds(0);
            else avgStay.setAvgDurationSeconds(dailyTime/(long)dailyPeople);
            totalTime+=dailyTime;

            dailyStayAvg.add(avgStay);



            List<User> users = boothLogService.getUserByBoothIDAndAccountAndDate(boothID, date);
            for(User user : users) totalContent += contentLogService.getContentNumberByBoothIDAndAccount(boothID, user.getUserAccount(), date);

        }
        overview.setNewVisitorsLast7Days(totalPeople);


        if(totalPeople == 0){
            rate.setParticipationRate(0.0);
            rate.setAiUsageRate(0.0);
        }else if(totalContent == 0) rate.setParticipationRate(0.0);
        else {
            totalAvg = totalTime/totalPeople;

            double pr = Math.round(((double) totalContent / totalPeople / 7 / 11) * 10.0) / 10.0;
            double ar = Math.round(((double) usedAI / totalPeople / 7) * 10.0) / 10.0;
            rate.setParticipationRate(pr);
            rate.setAiUsageRate(ar);
        }


        DataResponse response = new DataResponse();
        response.setOverview(overview);
        response.setDailyVisitor(dailyVisitor);
        response.setDailyStayAverage(dailyStayAvg);
        response.setTotalAvgDurationSeconds(totalAvg);
        response.setEngagement(rate);
        return response;
    }
}

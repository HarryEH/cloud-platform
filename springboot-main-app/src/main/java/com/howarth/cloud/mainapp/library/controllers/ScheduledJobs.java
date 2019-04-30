package com.howarth.cloud.mainapp.library.controllers;

import com.howarth.cloud.mainapp.library.database.DiamondRepository;
import com.howarth.cloud.mainapp.library.database.IcRepository;
import com.howarth.cloud.mainapp.library.database.RhhRepository;
import com.howarth.cloud.mainapp.library.database.model.DiamondStatistic;
import com.howarth.cloud.mainapp.library.database.model.IcStatistic;
import com.howarth.cloud.mainapp.library.database.model.RhhStatistic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Component
public class ScheduledJobs {

    private final int ERR = -1;
    private DiamondRepository diamondRepository;
    private IcRepository icRepository;
    private RhhRepository rhhRepository;

    public ScheduledJobs(DiamondRepository diamondRepository, IcRepository icRepository, RhhRepository rhhRepository) {
        this.diamondRepository = diamondRepository;
        this.icRepository = icRepository;
        this.rhhRepository = rhhRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void scheduleTaskDiamond() {
        DiamondStatistic dia = new DiamondStatistic(genericLibraryGet("diamond"));
        diamondRepository.save(dia);
    }

    @Scheduled(fixedRate = 30000)
    public void scheduleTaskIc() {
        IcStatistic ic = new IcStatistic(genericLibraryGet("ic"));
        icRepository.save(ic);
    }

    @Scheduled(fixedRate = 30000)
    public void scheduleTaskRhh() {
        RhhStatistic rhh = new RhhStatistic(genericLibraryGet("rhh"));
        rhhRepository.save(rhh);
    }


    private int genericLibraryGet(String library) {
        try {
            URLConnection lib = new URL("https://fast-hamlet-42269.herokuapp.com/" + library).openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(lib.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                String rtn = inputLine;
                in.close();
                try {
                    return Integer.parseInt(rtn);
                } catch (NumberFormatException ex) {
                    return ERR;
                }
            }
        } catch (MalformedURLException urlEx) {
            return ERR;
        } catch (IOException ioEx) {
            return ERR;
        }
        return ERR;
    }

}
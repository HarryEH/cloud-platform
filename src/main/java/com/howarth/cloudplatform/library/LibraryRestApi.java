package com.howarth.cloudplatform.library;

import com.howarth.cloudplatform.library.database.DiamondRepository;
import com.howarth.cloudplatform.library.database.IcRepository;
import com.howarth.cloudplatform.library.database.RhhRepository;
import com.howarth.cloudplatform.library.database.model.DiamondStatistic;
import com.howarth.cloudplatform.library.database.model.IcStatistic;
import com.howarth.cloudplatform.library.database.model.RhhStatistic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/library/api")
public class LibraryRestApi {

    private DiamondRepository diamondRepository;
    private IcRepository icRepository;
    private RhhRepository rhhRepository;

    public LibraryRestApi(DiamondRepository diamondRepository, IcRepository icRepository, RhhRepository rhhRepository) {
        this.diamondRepository = diamondRepository;
        this.icRepository = icRepository;
        this.rhhRepository = rhhRepository;
    }

    @GetMapping("/diamond")
    public DiamondStatistic diamond() {
        return diamondRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/ic")
    public IcStatistic ic() {
        return icRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/rhh")
    public RhhStatistic rhh() {
        return rhhRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/diamond/all")
    public List<DiamondStatistic> allDiamond() {
        return diamondRepository.findAll();
    }

    @GetMapping("/ic/all")
    public List<IcStatistic> allIc() {
        return icRepository.findAll();
    }

    @GetMapping("/rhh/all")
    public List<RhhStatistic> allRhh() {
        return rhhRepository.findAll();
    }
}

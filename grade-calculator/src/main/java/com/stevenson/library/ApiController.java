package com.stevenson.library;

import com.stevenson.library.database.model.IcStatistic;
import com.stevenson.library.database.model.RhhStatistic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import com.stevenson.library.database.DiamondRepository;
import com.stevenson.library.database.model.DiamondStatistic;
import com.stevenson.library.database.IcRepository;
import com.stevenson.library.database.RhhRepository;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

  private DiamondRepository diamondRepository;
  private IcRepository icRepository;
  private RhhRepository rhhRepository;

  public ApiController(DiamondRepository diamondRepository, IcRepository icRepository, RhhRepository rhhRepository) {
      this.diamondRepository = diamondRepository;
      this.icRepository = icRepository;
      this.rhhRepository = rhhRepository;
  }

    @GetMapping("/diamond")
    public DiamondStatistic diamond() {
        return diamondRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/ic")
    public IcStatistic ic(){
      return icRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/rhh")
    public RhhStatistic rhh(){
      return rhhRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/diamond/all")
    public List<DiamondStatistic> allDiamond() {
      return diamondRepository.findAll();
    }

    @GetMapping("/ic/all")
    public List<IcStatistic> allIc(){
      return icRepository.findAll();
    }

    @GetMapping("/rhh/all")
    public List<RhhStatistic> allRhh(){
      return rhhRepository.findAll();
    }
}

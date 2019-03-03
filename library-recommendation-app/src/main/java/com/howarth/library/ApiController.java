package com.howarth.library;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import com.howarth.library.database.DiamondRepository;
import com.howarth.library.database.DiamondStatistics;
import com.howarth.library.database.IcRepository;
import com.howarth.library.database.IcStatistics;
import com.howarth.library.database.RhhRepository;
import com.howarth.library.database.RhhStatistics;

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
    public String diamond() {
        int num = diamondRepository.findTopByOrderByIdDesc().getNumberOfPeople();
        return new Integer(num).toString();
    }

    @GetMapping("/ic")
    public String ic(){
      int num = icRepository.findTopByOrderByIdDesc().getNumberOfPeople();
      return new Integer(num).toString();
    }

    @GetMapping("/rhh")
    public String rhh(){
      int num = rhhRepository.findTopByOrderByIdDesc().getNumberOfPeople();
      return new Integer(num).toString();
    }

    @GetMapping("/diamond/all")
    public List<DiamondStatistics> allDiamond() {
      return diamondRepository.findAll();
    }

    @GetMapping("/ic/all")
    public List<IcStatistics> allIc(){
      return icRepository.findAll();
    }

    @GetMapping("/rhh/all")
    public List<RhhStatistics> allRhh(){
      return rhhRepository.findAll();
    }
}

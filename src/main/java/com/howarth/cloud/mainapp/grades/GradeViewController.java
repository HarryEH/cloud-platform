package com.howarth.cloud.mainapp.grades;

import com.howarth.cloud.mainapp.grades.database.GradeRepository;
import com.howarth.cloud.mainapp.grades.database.ModuleRepository;
import com.howarth.cloud.mainapp.grades.database.UserModuleRepository;
import com.howarth.cloud.mainapp.grades.database.model.Grade;
import com.howarth.cloud.mainapp.grades.database.model.Module;
import com.howarth.cloud.mainapp.grades.database.model.UserModule;
import com.howarth.cloud.mainapp.security.SecurityConstants;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.howarth.cloud.mainapp.security.JWTAuthorizationFilter.verifyCookieAuth;
import static com.howarth.cloud.mainapp.security.SecurityConstants.ACCESS_TOKEN;

@Controller
public class GradeViewController {
    private UserModuleRepository umr;
    private ModuleRepository mr;
    private GradeRepository gr;
    private String user = "empty";

    public GradeViewController(UserModuleRepository umr, ModuleRepository mr, GradeRepository gr) {
        this.umr = umr;
        this.mr = mr;
        this.gr = gr;
    }

    @GetMapping("/grade_calculator")
    public String loadIndex(Model model, HttpServletRequest request){
        informPaymentServer(request);
        user = verifyCookieAuth(request);

        if(umr.findDistinctByUsername(user) == null){
            UserModule um = new UserModule(user, new ArrayList<>(), 0);
            umr.save(um);
        }

        UserModule userToShow = umr.findDistinctByUsername(user);
        model.addAttribute("user", userToShow);
        model.addAttribute("modules", userToShow.getModules());
        model.addAttribute("newm", new Module(null,0));
        model.addAttribute("newg", new GradeHandler());


        return "grade_calc";
    }

    @PostMapping("/grade_calculator/updm")
    public String updateM(@ModelAttribute("newm") Module m){
        if(m.getId() != 0){
            Module oldM = mr.findOne(m.getId());
            if(oldM != null){
                oldM.setModuleName(m.getModuleName());
                oldM.setCredits(m.getCredits());
                mr.save(oldM);
                UserModule curUser = umr.findDistinctByUsername(user);
                curUser.updateCredits();
            }
        }else{
            //in case constructor ever changes to include extra code
            Module newM = new Module(m.getModuleName(), m.getCredits());
            mr.save(newM);
            UserModule curUser = umr.findDistinctByUsername(user);
            curUser.addModule(newM);
            umr.save(curUser);
        }

        return "redirect:/grade_calculator";
    }

    @PostMapping("/grade_calculator/updg")
    public String updateG(@ModelAttribute("newg") GradeHandler g){
        if(g.getId() != 0){
            Grade oldG = gr.findOne(g.getId());
            if(oldG != null){
                oldG.setName(g.getName());
                oldG.setGrade(g.getGrade());
                oldG.setMaxGrade(g.getMaxGrade());
                oldG.setWeight(g.getWeight());
                gr.save(oldG);
            }
        }else{
            //this constructor does actually include extra code
            Grade newG = new Grade(g.getName(), g.getGrade(), g.getMaxGrade(), g.getWeight());
            gr.save(newG);
            Module curModule = mr.findOne(g.getParentId());
            curModule.addGrade(newG);
            mr.save(curModule);
        }

        return "redirect:/grade_calculator";
    }

    @GetMapping("/grade_calculator/delete_grade")
    public String deleteGrade(@RequestParam(name="gradeId")long gId, @RequestParam(name="moduleId")long mId){
        Grade delG = gr.findOne(gId);
        Module parM = mr.findOne(mId);

        parM.delGrade(delG);
        mr.save(parM);
        gr.delete(gId);

        return "redirect:/grade_calculator";
    }

    @GetMapping("/grade_calculator/delete_module")
    public String deleteModule(@RequestParam(name="moduleId")long mId, @RequestParam(name="userId")long uId){
        Module delM = mr.findOne(mId);
        UserModule user = umr.findOne(uId);
        List<Grade> grades = delM.getGrades();

        user.delModule(delM);
        umr.save(user);
        delM.setGrades(new ArrayList<>());
        for(Grade g : grades){
            gr.delete(g.getId());
        }
        mr.delete(mId);

        return "redirect:/grade_calculator";
    }

    private void informPaymentServer(HttpServletRequest request) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {

            // This automatically gets the correct URL to sent the request to.
            final String URL = request.getScheme() + "://" + request.getServerName() +
                    ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
                    SecurityConstants.NEW_USAGE + "?app_name=grade_calculator&access_token=";

            //access token
            String token = "x.y.z";

            for (Cookie c : request.getCookies()) {
                if (c.getName().equals(ACCESS_TOKEN)) {
                    token = c.getValue();
                }
            }

            final String FINAL_URL = URL + token;

            HttpGet req = new HttpGet(FINAL_URL);

            httpClient.execute(req);

            httpClient.close();
        } catch (Exception ex) {
            // handle exception here
        }
    }
}

package controller;

import model.IPLookup;
import model.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repository.IPLookupRepository;
import repository.LoanRepository;
import util.IPWebServiceHelper;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by alex on 17.18.3.
 */
@Controller
@RequestMapping(value = "/loan")
public class LoanApplicationController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private IPLookupRepository ipLookupRepository;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public @ResponseBody List<LoanApplication> allLoanApplications(HttpServletRequest request) {
        return loanRepository.getLoans();
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<LoanApplication> loanApplicationsByUser(@RequestParam(value = "personalId") String personalId)
            throws InterruptedException {
        return loanRepository.byUser(personalId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createLoanApplication(@RequestParam("amount") Double amount,
                                      @RequestParam("personalId") String personalId,
                                      @RequestParam("term") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate term,
                                      @RequestParam("name") String name,
                                      @RequestParam("surname") String surname,
                                      HttpServletRequest request) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        String countryCode = determineCountryCode(request);
        loanRepository.createLoanApplication(amount, personalId, term, name, surname, countryCode, now);
    }


    private String determineCountryCode(HttpServletRequest request) throws IOException {
        String countryCode;
        try {
            IPLookup ipLookup = ipLookupRepository.getByIP(request.getRemoteAddr());
            countryCode = ipLookup.getCountryCode();
        }catch (NoResultException nre){
            countryCode = IPWebServiceHelper.getCountry(request.getRemoteAddr());
            ipLookupRepository.saveIPLookup(request.getRemoteAddr(), countryCode);
        }

        return countryCode;
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> errorHandler(Throwable t) {
        t.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

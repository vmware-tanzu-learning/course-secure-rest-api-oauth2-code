package example.cashcard;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class CashCardOwnerController {

    private Logger log = LoggerFactory.getLogger(CashCardOwnerController.class);
    private CashCardOwnerRepository cashCardOwnerRepository;

    public CashCardOwnerController(CashCardOwnerRepository cashCardOwnerRepository) {
        this.cashCardOwnerRepository = cashCardOwnerRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<CashCardOwner>> findAll(Authentication authentication) {
        // Audit log the request
        log.info("User {} is requesting all cash cards",authentication.getName());

        return ResponseEntity.ok(cashCardOwnerRepository.findAll());
    }
}

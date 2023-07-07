package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    /*
    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        return ResponseEntity.ok(page.getContent());
    }
    */

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long requestedId) {
        cashCardRepository.deleteById(requestedId);
        return ResponseEntity.noContent().build();
    }

    // New Methods


    // (1)
    /*
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(Authentication authentication) {
        var result = new ArrayList<CashCard>();

        cashCardRepository.findAll().forEach(cashCard -> {
            if (cashCard.owner().equals(authentication.getName())){
                result.add(cashCard);
            }
        });

        return ResponseEntity.ok(result);
    }
    */

    // (2)
    /*
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(Authentication authentication) {
        var result = cashCardRepository.findByOwner(authentication.getName());
        return ResponseEntity.ok(result);
    }
    */

    // (3)
    /*
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext SecurityContext securityContext) {
        var result = cashCardRepository.findByOwner(securityContext.getAuthentication().getName());
        return ResponseEntity.ok(result);
    }
    */

    // (4)
    /*
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext(expression="authentication.name") String owner) {
        var result = cashCardRepository.findByOwner(owner);
        return ResponseEntity.ok(result);
    }
    */

    // (5)
    /*
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(@CurrentOwner String owner) {
        var result = cashCardRepository.findByOwner(owner);
        return ResponseEntity.ok(result);
    }
    */

    // (6)

    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(@AuthenticationPrincipal Jwt jwt) {
        var result = cashCardRepository.findByOwner(jwt.getSubject());
        return ResponseEntity.ok(result);
    }

}
package example.cashcard;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.List;

public record CashCardOwner (@Id String owner, @MappedCollection(idColumn = "OWNER",keyColumn = "ID") List<CashCard> cashCards) {
}

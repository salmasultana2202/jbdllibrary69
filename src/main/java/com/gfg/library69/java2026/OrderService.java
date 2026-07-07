package com.gfg.library69.java2026;

import com.gfg.library69.domain.Book;
import com.gfg.library69.repository.BookRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    private final BookRepository bookRepository;
    private final AuditLogService auditLogService;

    public OrderService(BookRepository bookRepository, AuditLogService auditLogService) {
        this.bookRepository = bookRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional // REQUIRED (default)
    public void placeOrder(Book request) {
        Book order = bookRepository.save(request);

// Audit must persist EVEN IF placeOrder later throws and rolls back
        auditLogService.recordOrderPlacement(Long.valueOf(order.getId()));

        if (request.getCost() > 100_000) {
            throw new RuntimeException("Requires manual review");
// orderRepository.save() above WILL roll back,
// but auditLogService's REQUIRES_NEW transaction already committed
        }
    }

    // SELF-INVOCATION BUG:
    public void badPlaceOrder(Book request) {
        this.placeOrder(request); // calling "this." bypasses the proxy - NO transaction semantics applied!
    }
}

@Service
class AuditLogService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordOrderPlacement(Long orderId) {
// runs and commits in its own independent transaction
     //   auditRepository.save(new AuditEntry(orderId, Instant.now()));
    }
}

/*
6 types of propagations:
REQUIRED (default): joins an existing transaction, or starts a new one if none exists.
REQUIRES_NEW: always suspends any existing transaction and starts a brand-new, independent one
NESTED: creates a savepoint within the existing transaction
MANDATORY: throws an exception if no transaction is already active
SUPPORTS: joins if a transaction exists,else runs non-transactionally.
NOT_SUPPORTED: suspends any existing transaction, runs non-transactionally
*/

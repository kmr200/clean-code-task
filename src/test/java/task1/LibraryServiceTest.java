package task1;

import org.example.task1.event.LibraryEventListener;
import org.example.task1.exception.BookNotCheckedOutException;
import org.example.task1.exception.BookNotFoundException;
import org.example.task1.fine.impl.DailyFineStrategy;
import org.example.task1.model.Book;
import org.example.task1.model.LoanRecord;
import org.example.task1.model.Reservation;
import org.example.task1.repository.BookRepository;
import org.example.task1.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LibraryServiceTest {

    private BookRepository repository;
    private LibraryService service;
    private LibraryEventListener mockListener;

    @BeforeEach
    void setUp() {
        repository = new BookRepository();
        service = new LibraryService(repository, new DailyFineStrategy(0.50));
        mockListener = mock(LibraryEventListener.class);
        service.registerListener(mockListener);

        repository.addBook(new Book("BK001", "Clean Code", "Robert Martin"));
    }

    @Test
    void checkOut_availableBook_createsLoan() throws Exception {
        service.checkOutOrReserve("BK001", "USR001");

        assertTrue(repository.isCheckedOut("BK001"));
        verify(mockListener).onBookCheckedOut(any(LoanRecord.class));
    }

    @Test
    void checkOut_unavailableBook_createsReservation() throws Exception {
        service.checkOutOrReserve("BK001", "USR001");
        service.checkOutOrReserve("BK001", "USR002");

        verify(mockListener).onBookReserved(any(Reservation.class));
    }

    @Test
    void checkOut_nonExistentBook_throwsBookNotFoundException() {
        assertThrows(BookNotFoundException.class,
                () -> service.checkOutOrReserve("INVALID", "USR001"));
    }

    @Test
    void returnBook_checkedOutBook_removesLoan() throws Exception {
        service.checkOutOrReserve("BK001", "USR001");
        service.returnBook("BK001");

        assertFalse(repository.isCheckedOut("BK001"));
        verify(mockListener).onBookReturned(eq("BK001"), eq("USR001"));
    }

    @Test
    void returnBook_withReservationQueue_assignsToNextUser() throws Exception {
        service.checkOutOrReserve("BK001", "USR001");
        service.checkOutOrReserve("BK001", "USR002"); // goes to reservation
        service.returnBook("BK001");                   // should assign to USR002

        assertTrue(repository.isCheckedOut("BK001"));
        assertTrue(repository.findLoan("BK001")
                .map(l -> l.getUserId().equals("USR002"))
                .orElse(false));
    }

    @Test
    void returnBook_notCheckedOut_throwsException() {
        assertThrows(BookNotCheckedOutException.class,
                () -> service.returnBook("BK001"));
    }

    @Test
    void processOverdueBooks_overdueLoans_issuesFine() {
        // Simulate a loan made 20 days ago (overdue by 6 days at 14-day limit)
        LoanRecord overdueLoan = new LoanRecord("BK001", "USR001", LocalDate.now().minusDays(20));
        repository.saveLoan(overdueLoan);

        service.processOverdueBooks();

        // 6 days overdue * $0.50 = $3.00
        verify(mockListener).onFineIssued(eq("USR001"), eq("BK001"), eq(3.0));
    }
}

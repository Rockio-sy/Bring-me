package org.bringme.service.impl;

import org.bringme.dto.ReportDTO;
import org.bringme.model.Report;
import org.bringme.model.Request;
import org.bringme.repository.ReportRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(TestWatchersExtension.class)
public class ReportServiceTest {
    @Mock
    Converter converter;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given valid report with user id, when validateReportForm, then return true.")
    void givenValidReportWithUserId_whenValidateReportForm_thenReturnTrue() {
        //Arrange
        ReportDTO form = new ReportDTO(1, "Content");
        Request request = new Request(1L, 1, 2, 1, 1, 1, 2, "Comment", true, 1.2F, "Dollar");

        // Act
        when(requestRepository.getRequestById(any(Long.class))).thenReturn(Optional.of(request));


        boolean check = reportService.validateReportForm(form, 1L);
        assertTrue(check);
        verify(requestRepository, times(1)).getRequestById(any(Long.class));

    }

    @Test
    @DisplayName("Given invalid report form with user id, when validateReportForm, then return BAD_REQUEST exception")
    void givenInvalidReportFormWithUserId_whenValidateReportForm_thenReturnBAD_REQUESTException() {
        //Arrange
        ReportDTO form = new ReportDTO(1, "Content");
        Request request = new Request(1L, 3, 2, 1, 1, 1, 2, "Comment", true, 1.2F, "Dollar");

        // Act
        when(requestRepository.getRequestById(any(Long.class))).thenReturn(Optional.of(request));

        CustomException ex = assertThrows(CustomException.class, () -> reportService.validateReportForm(form, 1L));

        assertEquals("User doesn't belong to this request", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
        verify(requestRepository, times(1)).getRequestById(any(Long.class));
    }

    @Test
    @DisplayName("Given valid report form, when createNewReport, then return 'Request is not approved' Exception")
    void givenValidReportForm_whenCreateNewReport_thenReturnRequestIsNotApprovedException() {
        //Arrange
        ReportDTO form = new ReportDTO(1, "Content");
        Request request = new Request(1L, 1, 2, 1, 1, 1, 2, "Comment", false, 1.2F, "Dollar");

        // Act
        when(requestRepository.getRequestById(any(Long.class))).thenReturn(Optional.of(request));

        CustomException ex = assertThrows(CustomException.class, () -> reportService.validateReportForm(form, 1L));

        assertEquals("Request is not approved", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verify(requestRepository, times(1)).getRequestById(any(Long.class));
    }

    @Test
    @DisplayName("Given valid report form with user id, when create new report, then return ReportDTO")
    void givenValidReportFormWithUserId_whenCreateNewReport_thenReturnReportDTO() {
        ReportDTO form = new ReportDTO(1, "Content");
        Request request = new Request(1L, 3, 2, 1, 1, 1, 2, "Comment", true, 1.2F, "Dollar");

        when(requestRepository.getRequestById(any(Long.class))).thenReturn(Optional.of(request));
        when(reportRepository.save(any(Report.class))).thenReturn(1L);
        when(converter.reportToDTO(any(Report.class))).thenReturn(new ReportDTO(1L, 1, 1, 2, 3, "comment", "answer"));

        ReportDTO check = reportService.createNewReport(form, 2L);

        assertEquals(check.getId(), 1L);
        verify(requestRepository, times(1)).getRequestById(any(Long.class));
    }

    @Test
    @DisplayName("Given valid report form, when createNewReport, then return 'Error creating the report' Exception")
    void givenValidReportForm_whenCreateNewReport_thenReturnErrorCreatingTheReportException() {
        ReportDTO form = new ReportDTO(1, "Content");
        Request request = new Request(1L, 3, 2, 1, 1, 1, 2, "Comment", true, 1.2F, "Dollar");

        when(requestRepository.getRequestById(any(Long.class))).thenReturn(Optional.of(request));
        when(reportRepository.save(any(Report.class))).thenReturn(null);
        when(converter.reportToDTO(any(Report.class))).thenReturn(new ReportDTO(1L, 1, 1, 2, 3, "comment", "answer"));

        CustomException ex = assertThrows(CustomException.class, () -> reportService.createNewReport(form, 2L));
        assertEquals("Error creating the report", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        verify(requestRepository, times(1)).getRequestById(any(Long.class));
    }


    @Test
    @DisplayName("Given admin and report ids with admin answer, when answerReport, then no return")
    void givenAdminAndReportIdsWithAnswer_whenAnswerReport_thenNoReturn() {
        // Arrange
        Report model = new Report(1L, 1, 1, 2, 3, "comment", "");

        // Act
        when(reportRepository.getById(any(Long.class))).thenReturn(Optional.of(model));

        reportService.answerReport(1L, 1L, "answer");

        verify(reportRepository, times(1)).answerReport(1L, 1L, "answer");
    }

    @Test
    @DisplayName("Given admin and report ids with admin answer, when answerReport, then throw 'Report doesn't exist' exception ")
    void givenReportIdsWithAnswer_whenAnswerReport_thenThrowReportDoesNotExistsException() {
        // Act
        when(reportRepository.getById(any(Long.class))).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> reportService.answerReport(1L, 1L, "answer"));
        assertEquals("Report doesn't exist", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());


        verify(reportRepository, never()).answerReport(1L, 1L, "answer");
    }

    @Test
    @DisplayName("Given admin and report ids with admin answer, when answerReport, then throw 'Report already answered exception")
    void givenAdminAndReportIdsWithAdminAnswer_whenAnswerReport_thenThrowReportAlreadyAnsweredException() {
        // Arrange
        Report model = new Report(1L, 1, 1, 2, 3, "comment", "test");

        // Act
        when(reportRepository.getById(any(Long.class))).thenReturn(Optional.of(model));

        CustomException ex = assertThrows(CustomException.class, () -> reportService.answerReport(1L, 1L, "answer"));
        assertEquals("Report already answered", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());

        verify(reportRepository, never()).answerReport(1L, 1L, "answer");
    }
}






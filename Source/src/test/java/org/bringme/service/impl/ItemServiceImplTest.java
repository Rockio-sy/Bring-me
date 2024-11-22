package org.bringme.service.impl;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestWatchersExtension.class)
public class ItemServiceImplTest {

    private final String uploadDir = "/home/rockio/مخصص/Bring-me/Source/src/main/resources/photos";
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private Converter converter;
    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(itemService, "uploadDir", uploadDir);
    }

    @Test
    @DisplayName("Given valid multipart file, when saveTempFile, then return file name")
    void givenValidMultipartFile_whenSaveTempFile_thenReturnFileName() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        String originalFileName = "test_image.jpeg";
        byte[] fileContent = "Dummy Content".getBytes();
        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getBytes()).thenReturn(fileContent);

        // Act
        String savedFileName = itemService.saveTempFile(mockFile);

        // Assert
        assertTrue(savedFileName.startsWith("TEMP_"));
        assertTrue(savedFileName.endsWith(".jpeg"));

        // Verify the file exists in the upload directory
        Path savedFilePath = Paths.get(uploadDir, savedFileName);
        assertTrue(Files.exists(savedFilePath));

        // Verify the file content
        assertArrayEquals(fileContent, Files.readAllBytes(savedFilePath));

        // Clean up
        Files.deleteIfExists(savedFilePath);
    }


    @Test
    @DisplayName("Given multipart file without extension, when saving temp file, then return FormatException")
    void givenMultipartFileWithoutExtension_whenSaveTempFile_thenReturnFormatException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        String originalFileName = "test_image";
        byte[] fileContent = "Dummy Content".getBytes();
        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getBytes()).thenReturn(fileContent);

        // Act
        CustomException ex = assertThrows(CustomException.class, () -> itemService.saveTempFile(mockFile));
        assertEquals("File format error", ex.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
    }

    @Test
    @DisplayName("Given multipart file with invalid format, when saving temp file, then return TypeException")
    void givenMultipartFileWitInvalidFormat_whenSaveTempFile_thenReturnTypeException() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        String originalName = "test_image.txt";
        byte[] fileContent = "Dummy content".getBytes();
        when(mockFile.getOriginalFilename()).thenReturn(originalName);
        when(mockFile.getBytes()).thenReturn(fileContent);

        // Act
        CustomException ex = assertThrows(CustomException.class, () -> itemService.saveTempFile(mockFile));
        assertEquals("Image only", ex.getMessage());
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getStatus());
    }

    @Test
    @DisplayName("Given valid item, when saving item, then return ItemDTO")
    void givenValidItem_whenSaveItem_thenReturnItemDTO() throws IOException {

        MultipartFile mockFile = mock(MultipartFile.class);
        String originalFileName = "test_image.png";
        byte[] fileContent = "Dummy Content".getBytes();
        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getBytes()).thenReturn(fileContent);
        String imageName = itemService.saveTempFile(mockFile);

        ItemDTO dto = new ItemDTO("Test_name", 2, 3, 2, 2, 2, "No com", "Address", imageName, 1L);
        when(converter.DTOtoItem(dto)).thenReturn(new Item()); // Stub the DTO to Item conversion
        when(itemRepository.saveItem(any(Item.class))).thenReturn(1L); // Simulate repository save
        when(converter.itemToDTO(any(Item.class))).thenReturn(dto);

        ItemDTO savedItem = itemService.saveItem(dto);

        assertNotNull(savedItem);
        assertEquals("Test_name", savedItem.getName());
        assertEquals(1L, savedItem.getUser_id());
    }

    @Test
    @DisplayName("Given item with null image, when saving item, then return NotFoundException")
    void givenItemWitNullImage_whenSaveItem_thenReturnNotFoundException() {

        ItemDTO dto = new ItemDTO("Test_name", 1, 3, 2, 2, 2, "No com", "Address", null, 1L);
        when(converter.DTOtoItem(dto)).thenReturn(new Item()); // Stub the DTO to Item conversion
        when(itemRepository.saveItem(any(Item.class))).thenReturn(1L); // Simulate repository save
        when(converter.itemToDTO(any(Item.class))).thenReturn(dto);

        CustomException ex = assertThrows(CustomException.class, () -> itemService.saveItem(dto));
        assertTrue((ex.getMessage()).startsWith("image not found: "));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given item without valid image, when saving item, then return ImageFormatException")
    void givenItemWithoutValidImage_whenSaveItem_thenReturnImageFormatException() {

        ItemDTO dto = new ItemDTO("Test_name", 1, 3, 2, 2, 2, "No com", "Address", "ada", 1L);
        when(converter.DTOtoItem(dto)).thenReturn(new Item()); // Stub the DTO to Item conversion
        when(itemRepository.saveItem(any(Item.class))).thenReturn(1L); // Simulate repository save
        when(converter.itemToDTO(any(Item.class))).thenReturn(dto);

        CustomException ex = assertThrows(CustomException.class, () -> itemService.saveItem(dto));
        assertTrue((ex.getMessage()).startsWith("Image format error: "));
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative origin, when checking input, then return InvalidInputException")
    void givenNegativeOrigin_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", -1, 2, 2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative destination, when checking input, then return InvalidInputException")
    void givenNegativeDestination_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 4, -2, 2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative weight, when checking input, then return InvalidInputException")
    void givenNegativeWeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 4, 2, -2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative height, when checking input, then return InvalidInputException")
    void givenNegativeHeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 4, 2, 2, -2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative length, when checking input, then return InvalidInputException")
    void givenNegativeLength_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 4, 2, 2, 2, -2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given negative user id, when checking input, then return InvalidInputException")
    void givenNegativeUserId_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 2, 2, "no comment", "add", null, -2L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }


    @Test
    @DisplayName("Given zero origin, when checking input, then return InvalidInputException")
    void givenZeroOrigin_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 0, 2, 2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given zero destination, when checking input, then return InvalidInputException")
    void givenZeroDestination_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 0, 2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given zero weight, when checking input, then return InvalidInputException")
    void givenZeroWeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 0, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given zero height, when checking input, then return InvalidInputException")
    void givenZeroHeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 0, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given zero length, when checking input, then return InvalidInputException")
    void givenZeroLength_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 2, 0, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given zero user id, when checking input, then return InvalidInputException")
    void givenZeroUserId_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 2, 2, "no comment", "add", null, 0L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given same directions, when checking input, then return InvalidInputException")
    void givenSameDirections_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 1, 1, 2, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given three length, when checkInput, then return 'Invalid input' exception.")
    void givenThreeLength_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 2, 3, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given six weight, when checkInput, then return 'Invalid input' exception.")
    void givenSixWeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 6, 2, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given three length, when checking input, then return 'Invalid Input ' exception.")
    void givenThreeHeight_whenCheckInput_ReturnInvalidInputException() {
        ItemDTO dto = new ItemDTO("test", 2, 2, 2, 3, 2, "no comment", "add", null, 1L);
        CustomException ex = assertThrows(CustomException.class, () -> itemService.checkInput(dto));
        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}

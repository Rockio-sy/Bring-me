package org.bringme.service.impl;

import org.bringme.dto.ItemDTO;
import org.bringme.exceptions.CustomException;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.bringme.service.ItemService;
import org.bringme.utils.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.springframework.web.util.WebUtils.getRealPath;

@Service
public class ItemServiceImpl implements ItemService {

    @Value("${file.upload-dir}")
    private String uploadDir;


    private final ItemRepository itemRepository;
    private final Converter converter;

    public ItemServiceImpl(ItemRepository itemRepository, Converter converter) {
        this.itemRepository = itemRepository;
        this.converter = converter;
    }

    /**
     * Retrieves all items from the database. If no items are found, throws an exception with HTTP status
     * {@code 204 NO_CONTENT}.
     *
     * @return {@link List} of {@link ItemDTO} The list of items from the database, mapped to {@link ItemDTO}.
     * @throws CustomException If no data is found in the database, an exception with HTTP status
     *                         {@code 204 NO_CONTENT} is thrown.
     */
    @Override
    public List<ItemDTO> getAll() {
        // Get from database
        List<Item> dataBaseList = itemRepository.getAll();

        if (dataBaseList.isEmpty()) {
            throw new CustomException("No data", HttpStatus.NO_CONTENT);
        }

        List<ItemDTO> responseList = new ArrayList<>();
        for (Item model : dataBaseList) {
            ItemDTO responseItem = converter.itemToDTO(model);
            responseList.add(responseItem);
        }
        return responseList;
    }

    /**
     * Retrieves an item by its ID. If the item is not found, throws an exception with HTTP status
     * {@code 404 NOT_FOUND}.
     *
     * @param id The ID of the item to retrieve.
     * @return {@link ItemDTO} The item mapped to a {@link ItemDTO}.
     * @throws CustomException If the item with the provided ID is not found, an exception with HTTP status
     *                         {@code 404 NOT_FOUND} is thrown.
     */
    @Override
    public ItemDTO getItemById(Long id) {
        // Check if item exists
        Optional<Item> model = itemRepository.getById(id);
        if (model.isEmpty()) {
            throw new CustomException("Item not found", HttpStatus.NOT_FOUND);
        }
        return converter.itemToDTO(model.get());
    }

    /**
     * Saves a new item by validating the input, generating a modified file name for the photo,
     * moving the file to the upload directory, and saving the item in the database.
     *
     * @param itemDTO The {@link ItemDTO} object containing item data to be saved.
     * @return {@link ItemDTO} The saved item with the generated ID and updated photo name.
     * @throws CustomException If any error occurs during file handling or database operations,
     *                         an exception is thrown with appropriate HTTP status.
     */
    @Override
    public ItemDTO saveItem(ItemDTO itemDTO) {

        checkInput(itemDTO);
        try {
            // Change the file name
            String originalFileName = itemDTO.getPhoto();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String modifiedFileName = "ITEM_" + itemDTO.getName() + "-" + UUID.randomUUID() +
                    "-" + System.currentTimeMillis() + "-" + extension;

            // Encode the file name
            String encodedFileName = URLEncoder.encode(modifiedFileName, StandardCharsets.UTF_8);

            // Move the file to the upload directory
            String tempFilePath = uploadDir + File.separator + originalFileName;
            String filePath = uploadDir + File.separator + encodedFileName;
            Files.move(Paths.get(tempFilePath), Paths.get(filePath));

            // Convert to Item model
            Item newItem = converter.DTOtoItem(itemDTO);
            newItem.setPhoto(encodedFileName);

            // get the generated id
            Long generatedId = itemRepository.saveItem(newItem);

            // Convert to ItemDTO
            ItemDTO responseItemDTO = converter.itemToDTO(newItem);
            responseItemDTO.setId(generatedId);

            return responseItemDTO;
        } catch (IOException e) {
            throw new CustomException("Failed create item : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            throw new CustomException("image not found: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException("Image format error: " + e.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    /**
     * Saves a temporary file to the upload directory, generating a unique file name.
     *
     * @param image The {@link MultipartFile} to save.
     * @return {@link String} The file name of the saved temporary file.
     * @throws CustomException If any error occurs during file saving, an exception is thrown with HTTP status
     *                         {@code 500 INTERNAL_SERVER_ERROR}.
     */
    @Override
    public String saveTempFile(MultipartFile image) {
        try {
            String extension = extractExtension(image);
            String fileName = "TEMP_" + UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;
            String filePath = Paths.get(uploadDir, fileName).toString();
            Files.write(Paths.get(filePath), image.getBytes());
            return fileName;
        } catch (IOException e) {
            throw new CustomException("Failed to save file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalStateException e) {
            throw new CustomException("Upload directory issue: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filters and retrieves items based on the origin and destination country IDs.
     * If no items are found, throws an exception with HTTP status {@code 204 NO_CONTENT}.
     *
     * @param origin      The origin country ID.
     * @param destination The destination country ID.
     * @return {@link List} of {@link ItemDTO} The filtered list of items.
     * @throws CustomException If no items matching the filter are found, an exception with HTTP status
     *                         {@code 204 NO_CONTENT} is thrown.
     */
    @Override
    public List<ItemDTO> filterByCountries(int origin, int destination) {

        List<Item> data = itemRepository.filterByCountries(origin, destination);
        if (data.isEmpty()) {
            throw new CustomException("Not found", HttpStatus.NO_CONTENT);
        }
        List<ItemDTO> response = new ArrayList<>();
        for (Item item : data) {
            ItemDTO dto = converter.itemToDTO(item);
            response.add(dto);
        }
        return response;
    }

    /**
     * Validates the input for an item, checking length, weight, height, origin, destination, and user ID.
     * Throws an exception with HTTP status {@code 400 BAD_REQUEST} if invalid input is found.
     *
     * @param requestItem The {@link ItemDTO} object containing the item data to validate.
     * @throws CustomException If the input is invalid, an exception with HTTP status
     *                         {@code 400 BAD_REQUEST} is thrown.
     */
    @Override
    public void checkInput(ItemDTO requestItem) {
        if (requestItem.getLength() <= 0 || requestItem.getWeight() <= 0 || requestItem.getHeight() <= 0
                || requestItem.getLength() > 2 || requestItem.getWeight() > 5 || requestItem.getHeight() > 2
                || requestItem.getOrigin() == requestItem.getDestination() || requestItem.getUser_id() <= 0
                || requestItem.getOrigin() <= 0 || requestItem.getDestination() <= 0) {
            throw new CustomException("Invalid input", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Checks the media file (image) for validity by verifying if the file is empty, checking its metadata,
     * ensuring the file size is under 2MB, and confirming the content type is either PNG or JPEG.
     * If valid, saves the file temporarily and returns the file name.
     *
     * @param image The {@link MultipartFile} to check and save.
     * @return {@link String} The file name of the saved temporary file.
     * @throws CustomException If the file is invalid, an exception is thrown with appropriate HTTP status.
     */
    @Override
    public String checkMediaFile(MultipartFile image) {
        if (image.isEmpty()) {
            throw new CustomException("File is empty", HttpStatus.BAD_REQUEST);
        }

        if (image.getOriginalFilename() == null || image.getContentType() == null) {
            throw new CustomException("File metadata is invalid", HttpStatus.BAD_REQUEST);
        }

        // Check file size
        if (image.getSize() > (2 * 1024 * 1024)) {
            throw new CustomException("File size should be 2MB or less", HttpStatus.PAYLOAD_TOO_LARGE);
        }

        // Check file type
        String contentType = image.getContentType();
        if (!(contentType.startsWith("image/png") || contentType.startsWith("image/jpeg"))) {
            throw new CustomException("PNG or JPEG only", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        // Save temp file
        String tempFileName = saveTempFile(image);
        if (tempFileName == null) {
            throw new CustomException("Error uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return tempFileName;
    }

    /**
     * Extracts and returns the file extension of the provided image file.
     *
     * @param image The {@link MultipartFile} to extract the extension from.
     * @return {@link String} The file extension of the image.
     * @throws CustomException If the file format is invalid or the extension cannot be extracted,
     *                         an exception with HTTP status {@code 415 UNSUPPORTED_MEDIA_TYPE} is thrown.
     */
    @NotNull
    private String extractExtension(MultipartFile image) {
        assert uploadDir != null && !uploadDir.trim().isEmpty() : "Upload directory is not configured.";
        String extension;
        // Extract the extension
        try {
            String originalFileName = Objects.requireNonNull(image.getOriginalFilename());
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException("File format error", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!(extension.equals(".png")) && !(extension.equals(".jpeg")) && !(extension.equals("jpg"))) {
            throw new CustomException("Image only", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return extension;
    }

}

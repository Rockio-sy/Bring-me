package org.bringme.service.impl;

import kotlin.collections.EmptyList;
import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.bringme.service.ItemService;
import org.bringme.utils.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    @Override
    public List<ItemDTO> getAll() {
        // Get from database
        List<Item> dataBaseList = itemRepository.getAll();

        if (dataBaseList.isEmpty()) {
            return null;
        }

        List<ItemDTO> responseList = new ArrayList<>();
        for (Item model : dataBaseList) {
            ItemDTO responseItem = converter.itemToDTO(model);
            responseList.add(responseItem);
        }
        return responseList;
    }

    @Override
    public ItemDTO getItemById(Long id) {
        // Check if item exists
        Optional<Item> model = itemRepository.getById(id);
        return model.map(converter::itemToDTO).orElse(null);
        // Create DTO item

    }

    @Override
    public ItemDTO saveItem(ItemDTO itemDTO) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String saveTempFile(MultipartFile image) {
        try {
            // Extract the extension
            String originalFileName = Objects.requireNonNull(image.getOriginalFilename());
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // Change the file name to TEMPORARY
            String fileName = "TEMP_" + UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;
            // Create path to the file
            String filePath = uploadDir + File.separator + fileName;
            // Copy the file
            Files.write(Paths.get(filePath), image.getBytes());
            return fileName;
        } catch (IOException e) {
            System.out.println("ERROR:" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ItemDTO> filterByCountries(int origin, int destination) {

        List<Item> data = itemRepository.filterByCountries(origin, destination);
        if (data.isEmpty()) {
            return List.of();
        }
        List<ItemDTO> response = new ArrayList<>();
        for (Item item : data) {
            ItemDTO dto = converter.itemToDTO(item);
            response.add(dto);
        }
        return response;
    }

}

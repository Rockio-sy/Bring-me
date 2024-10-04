package org.bringme.service.impl;

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

    @Value("${file.upload-temp-dir}")
    private String uploadTempDir;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ItemRepository itemRepository;
    private final Converter converter;

    public ItemServiceImpl(ItemRepository itemRepository, Converter converter)
    {
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
        for (Item dataBaseItem : dataBaseList) {
            ItemDTO responseItem = new ItemDTO();
            responseItem.setId(dataBaseItem.getId());
            responseItem.setName(dataBaseItem.getName());
            responseItem.setOrigin(dataBaseItem.getOrigin());
            responseItem.setDestination(dataBaseItem.getDestination());
            responseItem.setWeight(dataBaseItem.getWeight());
            responseItem.setHeight(dataBaseItem.getHeight());
            responseItem.setLength(dataBaseItem.getLength());
            responseItem.setComments(dataBaseItem.getComments());
            responseItem.setDetailedOriginAddress(dataBaseItem.getDetailedOriginAddress());
            responseItem.setPhoto(dataBaseItem.getPhoto());
            responseItem.setUser_id(dataBaseItem.getUser_id());
            responseList.add(responseItem);
        }
        return responseList;
    }

    @Override
    public ItemDTO getItemById(Long id) {
        // Check if item exists
        Optional<Item> dataBaseItem = itemRepository.getById(id);
        if (dataBaseItem.isEmpty()) {
            return null;
        }

        System.out.println(dataBaseItem);

        // Create DTO item
        Item switchItem = dataBaseItem.get();
        ItemDTO responseItem = new ItemDTO();
        responseItem.setId(switchItem.getId());
        responseItem.setName(switchItem.getName());
        responseItem.setOrigin(switchItem.getOrigin());
        responseItem.setDestination(switchItem.getDestination());
        responseItem.setWeight(switchItem.getWeight());
        responseItem.setHeight(switchItem.getHeight());
        responseItem.setLength(switchItem.getLength());
        responseItem.setComments(switchItem.getComments());
        responseItem.setDetailedOriginAddress(switchItem.getDetailedOriginAddress());
        responseItem.setPhoto(switchItem.getPhoto());
        responseItem.setUser_id(switchItem.getUser_id());
        return responseItem;
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
            System.out.println(encodedFileName);

            // Move the file to the upload directory
            String tempFilePath = uploadTempDir + File.separator + originalFileName;
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
            String filePath = uploadTempDir + File.separator + fileName;
            // Copy the file
            Files.write(Paths.get(filePath), image.getBytes());
            return fileName;
        } catch (IOException e) {
            System.out.println("ERROR:" + e.getMessage());
            return null;
        }
    }

}

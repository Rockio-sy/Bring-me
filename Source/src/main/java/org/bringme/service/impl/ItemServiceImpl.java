package org.bringme.service.impl;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.bringme.service.ItemService;
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

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.getAll();
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> newItem = itemRepository.getById(id);
        return newItem.orElse(null);
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

            // Create new Item to insert it to database
            Item newItem = new Item(
                    null,
                    itemDTO.getName(),
                    itemDTO.getOrigin(),
                    itemDTO.getDestination(),
                    itemDTO.getWeight(),
                    itemDTO.getHeight(),
                    itemDTO.getLength(),
                    itemDTO.getComments(),
                    itemDTO.getDetailedOriginAddress(),
                    encodedFileName,
                    itemDTO.getUser_id()
            );

            // get the generated id
            Long generatedId = itemRepository.saveItem(newItem);

            // New itemDTO to send in response
            return new ItemDTO(
                    generatedId,
                    newItem.getName(),
                    newItem.getOrigin(),
                    newItem.getDestination(),
                    newItem.getWeight(),
                    newItem.getHeight(),
                    newItem.getLength(),
                    newItem.getComments(),
                    newItem.getDetailedOriginAddress(),
                    newItem.getPhoto(),
                    newItem.getUser_id());
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

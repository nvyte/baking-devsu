package com.devsu.hackerearth.backend.client.mapper;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientMapper {

    public static ClientDto mapToDto(Client client) {

        // new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address",
        // "9999999999", true);
        return ClientDto.builder()
                .id(client.getId())
                .dni(client.getDni())
                .name(client.getName())
                .address(client.getAddress())
                .gender(client.getGender())
                .age(client.getAge())
                .phone(client.getPhone())
                .password(client.getPassword())
                .isActive(client.isActive())
                .build();
    }

    public static void partialUpdate(Client origin, Client target) {
        if (origin.getId() != null) {
            target.setId(origin.getId());
        }
        if (origin.getDni() != null) {
            target.setDni(origin.getDni());
        }
        if (origin.getName() != null) {
            target.setName(origin.getName());
        }
        if (origin.getAddress() != null) {
            target.setAddress(origin.getAddress());
        }
        if (origin.getGender() != null) {
            target.setGender(origin.getGender());
        }
        if (origin.getAge() != 0) {
            target.setAge(origin.getAge());
        }
        if (origin.getPhone() != null) {
            target.setPhone(origin.getPhone());
        }
        if (origin.getPassword() != null) {
            target.setPassword(origin.getPassword());
        }
        target.setActive(origin.isActive());
    }

    public static Client mapToEntity(ClientDto clientDto) {
        Client client = new Client();

        client.setId(clientDto.getId());
        client.setName(clientDto.getName());
        client.setAddress(clientDto.getAddress());
        client.setDni(clientDto.getDni());
        client.setAge(clientDto.getAge());
        client.setGender(clientDto.getGender());
        client.setPhone(clientDto.getPhone());
        client.setActive(clientDto.isActive());
        client.setPassword(clientDto.getPassword());
        return client;
    }

}

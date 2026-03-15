package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.core.exception.ApiExceptionType;
import com.devsu.hackerearth.backend.client.core.exception.BusinessException;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		// Get all clients
		return clientRepository.findAll()
				.stream()
				.map(ClientMapper::mapToDto)
				.collect(Collectors.toList());

	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		return Optional.ofNullable(clientRepository.getOne(id))
				.map(ClientMapper::mapToDto)
				.orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND,
						"Cliente no encontrado por id ".concat(id.toString())));
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		var clientEntity = ClientMapper.mapToEntity(clientDto);
		var clientSaved = clientRepository.save(clientEntity);
		return ClientMapper.mapToDto(clientSaved);
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		// Update client
		var client = clientRepository.findById(clientDto.getId())
				.orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Client no encontrado."));
		var clientToUpdate = ClientMapper.mapToEntity(clientDto);
		ClientMapper.partialUpdate(clientToUpdate, client);
		var clientUpdated = clientRepository.save(client);
		return ClientMapper.mapToDto(clientUpdated);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		// Partial update account
		var client = clientRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Client no encontrado."));
		client.setActive(partialClientDto.isActive());
		var clientUpdated = clientRepository.save(client);
		return ClientMapper.mapToDto(clientUpdated);
	}

	@Override
	public void deleteById(Long id) {
		// Delete client
		clientRepository.deleteById(id);
	}

}

package HealthAPI.service;

import HealthAPI.converter.AddressConverter;
import HealthAPI.converter.ClientConverter;
import HealthAPI.dto.Client.ClientCreateDto;
import HealthAPI.dto.Client.ClientDto;
import HealthAPI.model.Client;
import HealthAPI.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientConverter clientConverter;
    private final AddressConverter addressConverter;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientConverter clientConverter, AddressConverter addressConverter) {
        this.clientRepository = clientRepository;
        this.clientConverter = clientConverter;
        this.addressConverter = addressConverter;
    }

    public ClientDto getClientByToken(String jwt) {
        Client client = clientRepository.findByTokens(jwt);
        return clientConverter.fromClientToClientDto(client);
    }

    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        client.markAsDeleted();
        clientRepository.save(client);
    }

    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findByDeleted(false);
        return clients.parallelStream()
                .map(clientConverter::fromClientToClientDto)
                .toList();
    }

    public ClientDto updateClient(Long id, ClientCreateDto clientCreateDto) {
        Client client = clientRepository.getReferenceById(id);
        client.setFullName(clientCreateDto.getFullName());
        client.setPhoneNumber(clientCreateDto.getPhoneNumber());
        client.setEmail(clientCreateDto.getEmail());
        client.setPassword(clientCreateDto.getPassword());
        client.setBirthDate(clientCreateDto.getBirthDate());
        client.setGender(clientCreateDto.getGender());
        client.setNIF(clientCreateDto.getNIF());
        client.setAddress(addressConverter.fromAddressDtoToAddress(clientCreateDto.getAddress()));
        clientRepository.save(client);
        return clientConverter.fromClientToClientDto(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow();
    }

}
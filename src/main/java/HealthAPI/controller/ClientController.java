package HealthAPI.controller;

import HealthAPI.dto.client.ClientDto;
import HealthAPI.dto.auth.RegisterRequest;
import HealthAPI.dto.client.ClientUpdateDto;
import HealthAPI.messages.Responses;
import HealthAPI.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok(Responses.DELETED_CLIENT.formatted(clientId));
    }

    @PatchMapping("/{clientId}")
    public ResponseEntity<ClientDto> updateAccount(@PathVariable Long clientId,
                                                   @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        ClientDto clientDto = clientService.updateClient(clientId, clientUpdateDto);
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping("/myaccount")
    public ResponseEntity<ClientDto> viewMyAccount(@NonNull HttpServletRequest header) {
        String user = header.getUserPrincipal().getName();
        ClientDto clientDto = clientService.getClientByEmail(user);
        return ResponseEntity.ok(clientDto);
    }

    @PatchMapping("/myaccount")
    public ResponseEntity<ClientDto> updateMyAccount(@NonNull HttpServletRequest header,
                                                     @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        String clientEmail = header.getUserPrincipal().getName();
        ClientDto updatedClient = clientService.updateClient(clientEmail, clientUpdateDto);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/view/{clientId}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Long clientId) {
        ClientDto client = clientService.getClientById(clientId);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

}
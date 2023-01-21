package io.reflectoring.service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.reflectoring.api.ClientApiDelegate;
import io.reflectoring.dao.ClientEntity;
import io.reflectoring.dao.ClientRepository;
import io.reflectoring.model.Client;
import io.reflectoring.model.ClientResponse;
import io.reflectoring.model.ClientResponseData;

@Service
public class ClientApiDelegateImpl implements ClientApiDelegate {
	
	Pattern regexForEmail = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	
	@Autowired
	ClientRepository clientRepository;
	
	private boolean isValidClientInput(Client body, ClientResponse clientResponse) {
		
		if (!InputValidator.isValidLenghtString(body.getEmail(), 1, 100))
			clientResponse.status(false).addMessagesItem("email lenght is not in (1,100) range");
		if (!InputValidator.isValidLenghtString(body.getName(), 1, 100))
			clientResponse.status(false).addMessagesItem("name lenght is not in (1,100) range");
		if (!regexForEmail.matcher(body.getEmail()).matches())
			clientResponse.status(false).addMessagesItem("regex validation failed for email");
		
		return clientResponse.getStatus();
	}
	
	@Override 
	public ResponseEntity<ClientResponse> createClient(Client body) {
		
		ClientResponse clientResponse = new ClientResponse().status(true);
		try {
			
			if (Boolean.FALSE.equals(isValidClientInput(body, clientResponse)))
				return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
			
			ClientEntity clientEntity = clientRepository.findByEmail(body.getEmail());
					
			if (clientEntity == null) {
				UUID uuid = clientRepository.save(new ClientEntity(body)).getUuid();
				clientResponse.status(true).addMessagesItem("client creation OK").data(new ClientResponseData().uuid(uuid.toString()));
				return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
			}
			else {
				clientResponse.status(false).addMessagesItem("client cannot be created, it already exists: "+clientEntity);
				return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<>(clientResponse.status(false).addMessagesItem(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	

}

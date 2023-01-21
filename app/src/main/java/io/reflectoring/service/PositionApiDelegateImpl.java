package io.reflectoring.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.reflectoring.api.PositionApiDelegate;
import io.reflectoring.dao.ClientEntity;
import io.reflectoring.dao.ClientRepository;
import io.reflectoring.dao.PositionEntity;
import io.reflectoring.dao.PositionRepository;
import io.reflectoring.model.Client;
import io.reflectoring.model.ClientResponse;
import io.reflectoring.model.Position;
import io.reflectoring.model.PositionFinalResponse;
import io.reflectoring.model.PositionResponse;

@Service
public class PositionApiDelegateImpl implements PositionApiDelegate {
	
	Pattern regexUuid = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	
	@Autowired
	PositionRepository positionRepository;
	@Autowired
	ClientRepository clientRepository;
	
	private String getPositionUrl(Long id) {
		return "http://localhost:8080/position/"+id;
	}

	@Override 
	public ResponseEntity<PositionFinalResponse> getPosition(Long positionId) {
		
		PositionFinalResponse positionFinalResponse = new PositionFinalResponse().status(true);
		
		try {
			
			Optional<PositionEntity> positionEntity = positionRepository.findById(positionId);
			
			if (positionEntity.isPresent() && !positionEntity.isEmpty()) {
				positionFinalResponse.addDataItem(new PositionResponse()
						.id(positionEntity.get().getId())
						.location(positionEntity.get().getLocation())
						.name(positionEntity.get().getName())
						.url(getPositionUrl(positionId)));
				
				return new ResponseEntity<>(positionFinalResponse, HttpStatus.OK);
			} else 
				return new ResponseEntity<>(positionFinalResponse.status(false)
						.addMessagesItem("position does not exist: "+getPositionUrl(positionId)), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(positionFinalResponse.status(false).addMessagesItem(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private boolean isValidPositionInput(String keyword, String location, PositionFinalResponse positionFinalResponse) {
		
		if (!InputValidator.isValidLenghtString(keyword, 1, 50))
			positionFinalResponse.status(false).addMessagesItem("keyword lenght is not in (1,50) range: "+ keyword);
		if (!InputValidator.isValidLenghtString(location, 1, 50))
			positionFinalResponse.status(false).addMessagesItem("location lenght is not in (1,50) range: "+ location);
		
		return positionFinalResponse.getStatus();
	}

	
	@Override
	public ResponseEntity<PositionFinalResponse> searchPosition(String keyword, String location, String uuid) {
		
		PositionFinalResponse positionFinalResponse = new PositionFinalResponse().status(true);
		
		try {
			
			if (!regexUuid.matcher(uuid).matches()) {
				return new ResponseEntity<>(positionFinalResponse
						.status(false)
						.addMessagesItem("error during uuid regex validation: "+uuid),
						HttpStatus.BAD_REQUEST);
			}
			
			UUID convertedUuid = InputValidator.getValidatedUuid(uuid);
			
			if (convertedUuid == null)
				return new ResponseEntity<>(positionFinalResponse
						.status(false)
						.addMessagesItem("error during uuid cast: "+uuid),
						HttpStatus.BAD_REQUEST);
			
			if (Boolean.FALSE.equals(isValidPositionInput(keyword, location, positionFinalResponse)))
				return new ResponseEntity<>(positionFinalResponse, HttpStatus.BAD_REQUEST);
			
			
			if (isValidClient(convertedUuid)) {
				List<PositionEntity> positions =  positionRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(keyword, location);
				
				for(PositionEntity positionEntity : positions) {
					PositionResponse positionResponse = new PositionResponse()
							.id(positionEntity.getId())
							.location(positionEntity.getLocation())
							.name(positionEntity.getName())
							.url(getPositionUrl(positionEntity.getId()));
					positionFinalResponse.addDataItem(positionResponse);
				}
					
				return new ResponseEntity<>(positionFinalResponse.status(true), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(positionFinalResponse.status(false).addMessagesItem("uuid does not exists: "+uuid), HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<>(positionFinalResponse.status(false).addMessagesItem(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	private boolean isValidPositionInput(Position body, PositionFinalResponse positionFinalResponse) {

		if (!InputValidator.isValidLenghtString(body.getName(), 0, 50))
			positionFinalResponse.status(false).addMessagesItem("name lenght is not in (0,50) range: "+ body.getName());
		if (!InputValidator.isValidLenghtString(body.getLocation(), 0, 50))
			positionFinalResponse.status(false).addMessagesItem("location lenght is not in (0,50) range: "+ body.getLocation());
		
		return positionFinalResponse.getStatus();
	}
		
	
	@Override
	public ResponseEntity<PositionFinalResponse> createPosition(Position body) {
		
		PositionFinalResponse positionFinalResponse = new PositionFinalResponse().status(true);
		
		try {
			
			if (Boolean.FALSE.equals(isValidPositionInput(body, positionFinalResponse)))
				return new ResponseEntity<>(positionFinalResponse, HttpStatus.BAD_REQUEST);
								
			UUID convertedUuid = InputValidator.getValidatedUuid(body.getUuid());
			
			if (convertedUuid == null)
				return new ResponseEntity<>(positionFinalResponse
						.status(false)
						.addMessagesItem("error during uuid cast: "+body.getUuid()),
						HttpStatus.BAD_REQUEST);
			
			
			if (isValidClient(convertedUuid)) {
				PositionEntity positionEntity = positionRepository.save(new PositionEntity(body));
				positionFinalResponse.addDataItem(
						new PositionResponse()
						.id(positionEntity.getId())
						.location(positionEntity.getLocation())
						.name(positionEntity.getName())
						.url(getPositionUrl(positionEntity.getId())));
						
				return new ResponseEntity<>(positionFinalResponse, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(positionFinalResponse.addMessagesItem("uuid does not exists: "+body.getUuid()), HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<>(positionFinalResponse.status(false).addMessagesItem(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private boolean isValidClient(UUID uuid) {
		ClientEntity clientEntity = clientRepository.findByUuid(uuid);
		return clientEntity != null;
	}
}

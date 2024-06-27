package br.com.api.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.data.vo.v1.PersonVO;
import br.com.api.data.vo.v2.PersonVOV2;
import br.com.api.exceptions.ResourceNotFoundException;
import br.com.api.mapper.DozerMapper;
import br.com.api.mapper.custom.PersonMapper;
import br.com.api.model.Person;
import br.com.api.repositories.PersonRepository;

@Service
public class PersonServices {
	
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll(){	
		logger.info("Finding all people.");//buscando todas pessoas
	
		return DozerMapper.ParseListObject(findAll(), PersonVO.class);
	}
	

	public PersonVO findById(Long id) {
		logger.info("Finding one person!");//buscando uma pessoa
	
		var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));//não encontrado registros para este id.
		return DozerMapper.ParseObject(entity, PersonVO.class);
	}
	
	public PersonVO create(PersonVO person) {
		logger.info("Creating one person!");
		
		var entity = DozerMapper.ParseObject(person, Person.class);
		var vo = DozerMapper.ParseObject(repository.save(entity), PersonVO.class);
		return vo;
	}

	public PersonVOV2 createv2(PersonVOV2 person) {
		logger.info("Creating one person with v2!");
		
		var entity = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Udating one person!");
		
		var entity = repository.findById(person.getId())
		 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		 		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());//macho/masculino
			
		var vo = DozerMapper.ParseObject(repository.save(entity), PersonVO.class);
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		var entity = repository.findById(id)
				 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}
}

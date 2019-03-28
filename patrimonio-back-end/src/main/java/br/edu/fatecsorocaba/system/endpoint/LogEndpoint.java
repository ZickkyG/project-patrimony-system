package br.edu.fatecsorocaba.system.endpoint;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.Log;
import br.edu.fatecsorocaba.system.repository.LogRepository;

@RestController
@RequestMapping("logs")
public class LogEndpoint {

	@Autowired
	private LogRepository repository;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Log log) {
		log = repository.save(log);
		return new ResponseEntity<>(log, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIflogExists(id);
		Optional<Log> log = repository.findById(id);
		return new ResponseEntity<>(log, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIflogExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody Log log) {
		verifyIflogExists(log.getLogId());
		repository.save(log);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void verifyIflogExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Log with ID " + id + " not found.");
	}

}
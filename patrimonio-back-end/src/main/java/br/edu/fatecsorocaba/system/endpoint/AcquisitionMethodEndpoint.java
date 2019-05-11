package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.AcquisitionMethod;
import br.edu.fatecsorocaba.system.repository.AcquisitionMethodRepository;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@RestController
@RequestMapping("acquisitionmethods")
public class AcquisitionMethodEndpoint {
	@Autowired
	private AcquisitionMethodRepository repository;

	@GetMapping
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfacquisitionMethodExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody AcquisitionMethod acquisitionMethod) {
		return new ResponseEntity<>(repository.save(acquisitionMethod), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIfacquisitionMethodExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody AcquisitionMethod acquisitionMethod) {
		verifyIfacquisitionMethodExists(acquisitionMethod.getAcquisitionMethodId());
		repository.save(acquisitionMethod);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfacquisitionMethodExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("AcquisitionMethod with ID " + id + " not found.");
	}
}

package pl.piosdamian.homecontroller.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piosdamian.homecontroller.interfaces.rest.dto.info.SystemInfoDTO;

@RestController
@RequestMapping("info")
public class SystemInfo {
	@Autowired
	private SystemInfoDTO info;

	@GetMapping
	public ResponseEntity<SystemInfoDTO> getCoreVersion() {
		return ResponseEntity.ok().body(info);
	}
}

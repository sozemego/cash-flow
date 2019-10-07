package com.soze.clock;

import com.soze.common.dto.Clock;
import com.soze.common.json.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.Instant;
import java.util.Map;

@Service
public class ClockRepository {

	private static final Logger LOG = LoggerFactory.getLogger(ClockRepository.class);

	private static final String FILE = "clock.json";

	private long startTime;

	@PostConstruct
	public void startup() throws Exception {
		LOG.info("ClockRepository init...");
		File file = FileUtils.getFile(FILE);
		Map<String, Object> json = JsonUtils.parse(file, Map.class);

		if ((long) json.get("startTime") == -1) {
			startTime = Instant.now().toEpochMilli();
			json.put("startTime", startTime);
			FileUtils.write(FileUtils.getFile(FILE), JsonUtils.serialize(json));
		}
		LOG.info("Loaded startTime = {}", startTime);
	}

	public long getStartTime() {
		return startTime;
	}
}

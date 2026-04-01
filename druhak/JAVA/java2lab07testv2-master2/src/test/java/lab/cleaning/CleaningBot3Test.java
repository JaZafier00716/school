package lab.cleaning;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TestAppender;

class CleaningBot3Test {

	StructureHelper helper = StructureHelper.getInstance(CleaningBot3Test.class);

	@Test
	void testSectorExist() {
		assertThat(CleaningBot3Test.class, new ClassExist("Sector"));
	}

	@Test
	void testSectorIsRecordExist() throws ClassNotFoundException {
		Class<?> sector = helper.getClass("Sector");
		assertTrue(sector.isRecord(), "Sector has to be java record.");
		assertThat(CleaningBot3Test.class, new ClassExist("Sector"));
	}

	@Test
	void testSectorContainsMethodGenerate() throws ClassNotFoundException {
		Class<?> sector = helper.getClass("Sector");
		assertThat(sector, new HasMethod("generate", sector));
	}

	@Test
	void testGenerateSectors() {
		assertThat(new CleaningBot().generateSectorsToClean(), Matchers.hasSize(10));
	}

	@Test
	void testGenerateSectorsContainsSector() throws ClassNotFoundException {
		assertEquals(helper.getClass("Sector"), new CleaningBot().generateSectorsToClean().getFirst().getClass(), "List returned by method generateSectorsToClean, have to contains objects of type Sector.");
	}

	@Test
	void testMoveAndCleanLogAll() {
		Configurator.setRootLevel(Level.ALL);
		final TestAppender appender = new TestAppender("unittest", null, null, false, null);
		appender.start();
		LoggerContext context = LoggerContext.getContext(false);
		Logger logger = context.getRootLogger();
		logger.addAppender(appender);

		try {
			CleaningBot cleaningBot = new CleaningBot();
			cleaningBot.moveAndClean(cleaningBot.generateSectorsToClean());
			Map<Level, List<LogEvent>> logMap = appender.getEvents().stream()
					.collect(Collectors.toMap(LogEvent::getLevel, e -> new LinkedList<>(List.of(e)), (value, n) -> {
						value.addAll(n);
						return value;
					}));

			int infoLogCount = logMap.getOrDefault(Level.INFO, Collections.emptyList()).size()
			+ logMap.getOrDefault(Level.DEBUG, Collections.emptyList()).size();
			assertTrue(infoLogCount >= 11,
					"At least 11 messages in proper level for informativ log shout be outputed. But only " + infoLogCount + " outputed");
			int traceLogCount = logMap.getOrDefault(Level.TRACE, Collections.emptyList()).size();
			assertTrue(
					 traceLogCount >= 10,
					"At least 10 message in proper level for most detailed level about execution shout be outputed. But only " + traceLogCount + " outputed");
		} finally {
			logger.removeAppender(appender);
			appender.stop();
		}
	}

	@Test
	void testCleanLogComputeMessageOnlyIfReallyPrintedException() {
		Configurator.setRootLevel(Level.OFF);
		new CleaningBot().moveAndClean(List.of(new Object() {
			@Override
			public String toString() {
				throw new UnsupportedOperationException();
			}
		}));
	}

}

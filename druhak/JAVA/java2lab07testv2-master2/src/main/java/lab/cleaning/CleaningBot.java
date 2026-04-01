package lab.cleaning;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CleaningBot {

    public static void main(String[] args) {
        new CleaningBot().run();
    }

    public void run() {
        Configurator.setRootLevel(Level.INFO);
        moveAndClean(generateSectorsToClean());
    }

    // Java Record s statickou metodou generate()
    public record Sector(int x, int y) {
        public static Sector generate() {
            Random rand = new Random();
            return new Sector(rand.nextInt(100), rand.nextInt(100));
        }
    }

    // Generování 10 náhodných sektorů pomocí Stream + Functional Interface
    public List<Sector> generateSectorsToClean() {
        return Stream.generate(Sector::generate)  // Supplier<Sector> – functional interface
                .limit(10)
                .toList();
    }

    // Logování s kontrolou úrovně (efektivita logování)
    public void moveAndClean(List<?> sectors) {
        log.info("Počet sektorů k uklízení: {}", sectors::size); // lambda pro lazy evaluation

        for (Object sector : sectors) {
            // Nejdetailnější úroveň = TRACE – sestaví se jen pokud je TRACE aktivní
            if (log.isTraceEnabled()) {
                log.trace("Přesun na sektor: {}", sector);
            }

            // Běžná úroveň = INFO
            log.info("Sektor {} byl uklizen.", sector);
        }
    }
}
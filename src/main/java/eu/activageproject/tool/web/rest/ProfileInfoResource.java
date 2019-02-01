package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.config.DefaultProfileUtil;

import io.github.simlife.config.SimlifeProperties;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Resource to return information about the currently running Spring profiles.
 */
@RestController
@RequestMapping("/api")
public class ProfileInfoResource {

    private final Environment env;

    private final SimlifeProperties jSimlifeProperties;

    public ProfileInfoResource(Environment env, SimlifeProperties jSimlifeProperties) {
        this.env = env;
        this.jSimlifeProperties = jSimlifeProperties;
    }

    @GetMapping("/profile-info")
    public ProfileInfoVM getActiveProfiles() {
        String[] activeProfiles = DefaultProfileUtil.getActiveProfiles(env);
        return new ProfileInfoVM(activeProfiles, getRibbonEnv(activeProfiles));
    }

    private String getRibbonEnv(String[] activeProfiles) {
        String[] displayOnActiveProfiles = jSimlifeProperties.getRibbon().getDisplayOnActiveProfiles();
        if (displayOnActiveProfiles == null) {
            return null;
        }
        List<String> ribbonProfiles = new ArrayList<>(Arrays.asList(displayOnActiveProfiles));
        List<String> springBootProfiles = Arrays.asList(activeProfiles);
        ribbonProfiles.retainAll(springBootProfiles);
        if (!ribbonProfiles.isEmpty()) {
            return ribbonProfiles.get(0);
        }
        return null;
    }

    class ProfileInfoVM {

        private String[] activeProfiles;

        private String ribbonEnv;

        ProfileInfoVM(String[] activeProfiles, String ribbonEnv) {
            this.activeProfiles = activeProfiles;
            this.ribbonEnv = ribbonEnv;
        }

        public String[] getActiveProfiles() {
            return activeProfiles;
        }

        public String getRibbonEnv() {
            return ribbonEnv;
        }
    }
}

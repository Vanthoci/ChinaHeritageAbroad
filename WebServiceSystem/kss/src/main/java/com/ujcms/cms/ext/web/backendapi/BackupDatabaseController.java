package com.ujcms.cms.ext.web.backendapi;

import com.ujcms.cms.core.aop.annotations.OperationLog;
import com.ujcms.cms.core.aop.enums.OperationType;
import com.ujcms.cms.core.domain.Config;
import com.ujcms.cms.core.domain.Site;
import com.ujcms.cms.core.support.Contexts;
import com.ujcms.cms.core.support.Props;
import com.ujcms.commons.file.FilesEx;
import com.ujcms.commons.file.WebFile;
import com.ujcms.commons.web.PathResolver;
import com.ujcms.commons.web.Responses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ujcms.cms.ext.web.backendapi.DatabaseHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ujcms.cms.core.support.UrlConstants.BACKEND_API;

/**
 * @author PONY
 */
@RestController("backendBackupDatabaseController")
@RequestMapping(BACKEND_API + "/ext/backup-database")
public class BackupDatabaseController extends AbstractWebFileController {
    public BackupDatabaseController(PathResolver pathResolver, Props props) {
        super(pathResolver, props);
    }

    @Override
    protected List<String> getExcludes() {
        return Arrays.asList(EXCLUDES_WEB_INF, EXCLUDES_META_INF, EXCLUDES_CP, EXCLUDES_TEMPLATES, EXCLUDES_UPLOADS);
    }

    @Override
    protected String getSubDir() {
        String staticBase = Contexts.getCurrentSite().getStaticBase();
        if (StringUtils.isBlank(staticBase)) {
            return FilesEx.SLASH;
        }
        return staticBase;
    }

    private Config.Storage getBackupStorage() {
        Site site = Contexts.getCurrentSite();
        Config.Storage storage = site.getConfig().getHtmlStorage();
        storage.setPath("/backup");
        storage.setUrl("/backup");
        return storage;
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('backupDatabase:list','*')")
    public List<WebFile> list(@RequestParam(defaultValue = "/") String parentId,
                              @RequestParam(defaultValue = "false") boolean isDir,
                              @RequestParam(required = false) String name,
                              @RequestParam(defaultValue = "name") String sort) {
        // Config.Storage backupDatabasStorage = new Config.Storage(Config.Storage.TYPE_LOCAL, "", "");
        return super.list(parentId, isDir, name, sort, getBackupStorage());
    }

    @GetMapping("show")
    @PreAuthorize("hasAnyAuthority('backupDatabase:show','*')")
    public WebFile show(String id) {
        Site site = Contexts.getCurrentSite();
        return super.show(id, site.getConfig().getHtmlStorage());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('backupDatabase:create','*')")
    @OperationLog(module = "backupDatabase", operation = "backup-database", type = OperationType.CREATE)
    public ResponseEntity<Responses.Body> backUpDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        try { 
            databaseHelper.quickBackup();
            return Responses.ok();
        } catch (Exception e) {
            return Responses.failure(e.getMessage());
        }
    }


    @PutMapping("restore")
    @PreAuthorize("hasAnyAuthority('backupDatabase:create','*')")
    @OperationLog(module = "backupDatabase", operation = "restore", type = OperationType.CREATE)
    public ResponseEntity<Responses.Body> restore(@RequestParam("name") String name) {
        System.out.println("restoring " + name);
        DatabaseHelper databaseHelper = new DatabaseHelper();
        try { 
            databaseHelper.quickRestore(name);
            System.out.println("restored OK");
            return Responses.ok();
        } catch (Exception e) {
            return Responses.failure(e.getMessage());
        }
    }


    @PostMapping("download-zip")
    @PreAuthorize("hasAnyAuthority('backupDatabase:downloadZip','*')")
    public void downloadZip(@RequestBody @Valid WebFileDownloadParams params, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        super.downloadZip(params, getBackupStorage(), request, response);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('backupDatabase:update','*')")
    @OperationLog(module = "backupDatabase", operation = "update", type = OperationType.UPDATE)
    public ResponseEntity<Responses.Body> update(@RequestBody @Valid WebFileParams params) {
        Site site = Contexts.getCurrentSite();
        return super.update(params, site.getConfig().getHtmlStorage());
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('backupDatabase:delete','*')")
    @OperationLog(module = "backupDatabase", operation = "delete", type = OperationType.DELETE)
    public ResponseEntity<Responses.Body> delete(@RequestBody List<String> ids) {
        return super.delete(ids, getBackupStorage());
    }
}

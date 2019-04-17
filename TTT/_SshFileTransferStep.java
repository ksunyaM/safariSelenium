package com.wba.rxrintegration.api.test;

import com.google.common.io.Files;
import com.oneleo.test.automation.core.FileTransferUtils;
import com.oneleo.test.automation.core.SSHUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class _SshFileTransferStep
{

    private String fileName = StringUtils.EMPTY;
    private String localPath = System.getProperty("user.dir") + File.separator;
    private String pathFileToUpload = StringUtils.EMPTY;
    private String localDownloadDir = StringUtils.EMPTY;
    private static File sftpRootFolder;
    private static File download_dir;
    private static File upload_dir;
    private File fileToUpload;
    private static final String FILE_NAME_ORIGIN = "file_origin.txt";
    private static final String DIR_SERVER_DL = "download";
    private static final String DIR_SERVER_UP = "upload";
    private static final String SAMPLE_TEXT = "Test SFTP";

    @Given("^An Existing file \"([^\"]*)\" into \"([^\"]*)\" folder$")

    public void an_Existing_file_into_folder(String fileName, String pathFile) throws Throwable {
        this.fileName = fileName;
        this.pathFileToUpload = this.localPath + pathFile + File.separator + this.fileName;
        fileToUpload = new File(this.pathFileToUpload);
        assertThat(fileToUpload.exists(), equalTo(Boolean.TRUE));
    }

    @Given("^An Existing file \"([^\"]*)\" on server$")
    public void an_Existing_file_on_server(String fileName) throws Throwable {
        this.fileName = fileName;
        assertThat((new File(download_dir.getAbsolutePath() + File.separator + this.fileName).exists()),
                equalTo(Boolean.TRUE));
    }

    @When("^I connect to the server$")
    public void i_connect_to_the_server() throws Throwable {
        assertThat(FileTransferUtils.sftp().template(), notNullValue());
    }

    @When("^I upload the file to the server into \"([^\"]*)\" folder$")
    public void i_upload_the_file_to_the_server_into_folder(String pathUploadDirServer) throws Throwable {
        FileTransferUtils.sftp().template().upload(this.pathFileToUpload, pathUploadDirServer);
    }

    @Then("^The server receives correctly the file and saves it$")
    public void the_server_receives_correctly_the_file_and_save_it() throws Throwable {
        String expectedText = Files.readFirstLine(fileToUpload, StandardCharsets.UTF_8);
        File fileOnServer = new File(upload_dir.getAbsolutePath() + File.separator + this.fileName);
        assertThat(fileOnServer.exists(), equalTo(Boolean.TRUE));
        String actualtext = Files.readFirstLine(fileOnServer, StandardCharsets.UTF_8);
        assertThat(actualtext, equalTo(expectedText));
    }

    @When("^I download the file from the server into \"([^\"]*)\" folder$")
    public void i_download_the_file_from_the_server_into_folder(String pathDownload) throws Throwable {
        this.localDownloadDir = pathDownload;
        FileTransferUtils.sftp().template().download(DIR_SERVER_DL + File.separator + this.fileName,
                this.localPath + this.localDownloadDir + File.separator);
    }

    @Then("^The file is download and saved correctly on local computer$")
    public void the_file_is_downloaded_and_saved_correctly_on_local_computer() throws Throwable {
        File downloadedFile = new File(this.localPath + this.localDownloadDir + File.separator + FILE_NAME_ORIGIN);
        assertThat(downloadedFile.exists(), equalTo(Boolean.TRUE));
        String text = Files.readFirstLine(downloadedFile, StandardCharsets.UTF_8);
        assertThat(text, equalTo(SAMPLE_TEXT));
    }


}
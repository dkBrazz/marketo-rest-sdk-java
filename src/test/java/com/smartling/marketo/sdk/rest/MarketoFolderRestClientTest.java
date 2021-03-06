package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.folder.GetFoldersCommand;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoFolderRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoFolderRestClient testedInstance;

    @Test
    public void shouldReturnFolder() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFoldersCommand.class))).willReturn(Collections.singletonList(folder));

        List<FolderDetails> folders = testedInstance.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 10, null);

        assertThat(folders).contains(folder);
    }
}
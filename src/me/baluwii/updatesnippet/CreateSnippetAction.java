package me.baluwii.updatesnippet;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import kotlin.text.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * JavaDoc this file!
 * Created: 13.08.2018
 *
 * @author Baluwii (paskutscha@gmail.com)
 */
public class CreateSnippetAction extends AnAction {

    @Override
    public void actionPerformed( AnActionEvent event ) {
        final Editor editor = CommonDataKeys.EDITOR.getData( event.getDataContext() );

        post( editor.getSelectionModel().hasSelection() ? editor.getSelectionModel().getSelectedText() : editor.getDocument().getText(),
                key -> BrowserUtil.browse( "https://hastebin.com/" + key ) );
    }

    private void post( String data, final Consumer<String> consumer ) {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost( "https://hastebin.com/documents" );

        try {
            post.setEntity( new StringEntity( data, Charsets.UTF_8 ) );

            final HttpResponse response = client.execute( post );
            final String key = EntityUtils.toString( response.getEntity() ).replace( "{\"key\":\"", "" ).replace( "\"}", "" );

            consumer.accept( key );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
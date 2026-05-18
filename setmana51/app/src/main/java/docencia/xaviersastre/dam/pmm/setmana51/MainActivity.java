package docencia.xaviersastre.dam.pmm.setmana51;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etBody;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.view.View root = findViewById(R.id.main);
        final int startPadding = root.getPaddingLeft();
        final int topPadding = root.getPaddingTop();
        final int endPadding = root.getPaddingRight();
        final int bottomPadding = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(startPadding, topPadding + bars.top, endPadding, bottomPadding + bars.bottom);
            return windowInsets;
        });

        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> enviarPost());
    }

    private void enviarPost() {
        String title = etTitle.getText().toString().trim();
        String body = etBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Omple tots els camps", Toast.LENGTH_SHORT).show();
            return;
        }

        PostRequest request = new PostRequest(title, body, 1);
        Call<PostResponse> call = RetrofitClient.getApiService().createPost(request);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "✅ Enviat correctament (ID: " + response.body().getId() + ")", Toast.LENGTH_LONG).show();
                } else {
                    // Error HTTP (4xx, 5xx)
                    Log.e("RETROFIT", "Codi HTTP: " + response.code());
                    Toast.makeText(MainActivity.this, "❌ Error del servidor: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                // Error de xarxa, DNS, timeout, sense internet
                Log.e("RETROFIT", "Error de xarxa", t);
                Toast.makeText(MainActivity.this, "❌ Error de connexió: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
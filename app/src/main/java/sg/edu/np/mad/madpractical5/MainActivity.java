package sg.edu.np.mad.madpractical5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Optional;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private User user;
    private DatabaseHandler db;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        user = getUserFromIntent().orElse(new User("John Doe", "MAD Developer", 0, false));

        setupFollowButton();
        setupProfileButton();
        setupWindowInsets();
    }

    private Optional<User> getUserFromIntent() {
        int userId = getIntent().getIntExtra("id", 0);
        return ListActivity.userList.stream().filter(u -> u.id == userId).findFirst();
    }

    private void setupFollowButton() {
        Button followBtn = findViewById(R.id.Button1);
        followBtn.setText(user.followed ? "UNFOLLOW" : "FOLLOW");

        followBtn.setOnClickListener(v -> {
            user.followed = !user.followed;
            db.updateUser(user); // Update the database

            followBtn.setText(user.followed ? "FOLLOW" : "UNFOLLOW");
            Toast.makeText(this, user.followed ? "Unfollowed" : "Followed", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupProfileButton() {
        findViewById(R.id.imageView1).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Profile")
                    .setMessage("MADness")
                    .setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss())
                    .setNegativeButton("VIEW", (dialog, which) -> {
                        String text = "MAD " + Math.abs(random.nextInt());
                        ((TextView) findViewById(R.id.textView2)).setText(text);
                    })
                    .create();

            alertDialog.show();
        });
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
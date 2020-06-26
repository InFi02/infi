package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.infi_project.R;
import com.example.infi_project.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import Models.Post;

public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    FirebaseUser firebaseUser;

    public Post_Adapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }



    @NonNull
    @Override
    public Post_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new Post_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Post_Adapter.ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPostimage())
              //  .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.post_image);

        if (post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }

        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
      //  isLiked(post.getPostid(), holder.like);
       // isSaved(post.getPostid(), holder.save);
       // nrLikes(holder.likes, post.getPostid());
       // getCommetns(post.getPostid(), holder.comments);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("userDetails").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    Users user = dataSnapshot.getValue(Users.class);
//                    Glide.with(mContext).load(user.getImage()).into(image_profile);
//                    username.setText(user.getUserName());
//                    publisher.setText(user.getUserName());
                    Glide.with(mContext).load(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString()).into(image_profile);
                    username.setText(Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString());
//                    publisher.setText(Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString());


                }
                else {
                    Log.d("Post_Adapter", "DataSnapshot doesn't exists");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image, like, comment, save, more;
        public TextView username, likes, publisher, description, comments;

        public ViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
           // save = itemView.findViewById(R.id.save);
            likes = itemView.findViewById(R.id.likes);
//            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
            //more = itemView.findViewById(R.id.more);
        }
    }


}
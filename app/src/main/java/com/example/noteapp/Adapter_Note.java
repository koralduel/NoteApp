package com.example.noteapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Adapter_Note extends RecyclerView.Adapter<Adapter_Note.ViewHolder>{

    List<Note> notes;
    LayoutInflater layoutInflater;
    ClickInterface clickInterface;

    public Adapter_Note(List<Note> notes, Context context) {
        this.notes = notes;
        this.layoutInflater = LayoutInflater.from(context);
        this.clickInterface = (NoteListPage)context;
    }

    @NonNull
    @Override
    public Adapter_Note.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= layoutInflater.inflate(R.layout.note_item,parent,false);
        return new ViewHolder(view,clickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Note.ViewHolder holder, int i) {

        String title = notes.get(i).getTitle();
        holder.title.setText(title);

        String creationDate = notes.get(i).getCreationDate();
        holder.creationDate.setText(creationDate);

        String body = notes.get(i).getBody();
        if(body.length()>20){
            holder.body.setText(body.substring(0,20));
        }
        else{
            holder.body.setText(body);
        }

    }

    public void setPosts(List<Note> s){
        Collections.sort(s, new Comparator<Note>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(Note o1, Note o2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate post1=LocalDate.parse(o1.getCreationDate(),formatter);
                LocalDate post2=LocalDate.parse(o2.getCreationDate(),formatter);
                return post1.isAfter(post2) ? -1:1;
            }
        });
        notes = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(notes!=null){
            return notes.size();
        }
        return 0;
    }

    public List<Note> getNotes(){
        return notes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, creationDate, body;

        public ViewHolder(@NonNull View viewItem, ClickInterface clickInterface) {
            super(viewItem);
            title = viewItem.findViewById(R.id.Tv_titleItem);
            creationDate = viewItem.findViewById(R.id.Tv_dateItem);
            body = viewItem.findViewById(R.id.Tv_bodyItem);

            viewItem.setOnClickListener(v -> {
                if (clickInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickInterface.OnItemClick(position);
                    }
                }
            });

        }
    }

}

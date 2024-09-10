    package com.example.rma_2_alma_kuduzovic;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Button;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.auth.FirebaseAuth;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
        private Context context;
        private List<CardModel> cardList;
        private List<CardModel> cardListFull;
        private UserCartDao userCartDao;
        private ExecutorService executorService = Executors.newSingleThreadExecutor();


        public CardAdapter(Context context, List<CardModel> cardList, UserCartDao userCartDao) {
            this.context = context;
            this.cardList = cardList;
            this.cardListFull = new ArrayList<>(cardList);
            this.userCartDao = userCartDao;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            CardModel card = cardList.get(position);
            holder.imageView.setImageResource(card.getImageResource());
            holder.titleView.setText(card.getTitle());
            holder.priceView.setText(context.getString(R.string.price_format, String.valueOf(card.getPrice())));
            holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(card);
                }
            });
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        public void filter(String query) {
            List<CardModel> filteredList = new ArrayList<>();
            for (CardModel card : cardListFull) {
                if (card.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(card);
                }
            }
            cardList = filteredList;
            notifyDataSetChanged();
        }

        private void addToCart(CardModel card) {
            UserCartModel userCartModel = new UserCartModel();
            userCartModel.setName(card.getTitle());
            userCartModel.setPrice(card.getPrice());

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userCartModel.setUserId(userId);


            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    userCartDao.insert(userCartModel);
                    Intent intent = new Intent(context, MyCartActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        public static class CardViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView titleView;
            TextView priceView;
            Button addToCartButton;

            public CardViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.cardImage);
                titleView = itemView.findViewById(R.id.cardTitle);
                priceView = itemView.findViewById(R.id.cardPrice);
                addToCartButton = itemView.findViewById(R.id.addToCartButton);
            }
        }


    }

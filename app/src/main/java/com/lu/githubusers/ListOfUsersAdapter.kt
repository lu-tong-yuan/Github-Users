package com.lu.githubusers

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lu.githubusers.model.UsersList
import de.hdodenhof.circleimageview.CircleImageView

class ListOfUsersAdapter(context: Context, val usersList: List<UsersList>?):RecyclerView.Adapter<ListOfUsersAdapter.MyViewHolder>() {
    private val layoutInflater: LayoutInflater? = LayoutInflater.from(context)
    private var usersLists: List<UsersList>? = usersList

    fun setUsers(usersLists:List<UsersList>) {
        this.usersLists = usersLists;
    }

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(user: UsersList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = layoutInflater!!.inflate(R.layout.item_view_users, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user : UsersList = usersLists!![position]
        holder.tvName.text = user.login
        if (user.site_admin!!){
            holder.tvPosition.visibility = View.VISIBLE
        } else{
            holder.tvPosition.visibility = View.GONE
        }
        Glide.with(holder.itemView.context)
            .load(user.avatar_url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAG_ERROR", e!!.message!!)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .placeholder(0x00000000)
            .into(holder.ivPhoto)

        holder.cvUser.setOnClickListener{
            onItemClickListener.onItemClick(user)
        }
    }

    override fun getItemCount(): Int {
        return usersLists!!.size

    }

    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivPhoto : CircleImageView = view.findViewById(R.id.ivPhoto)
        val tvName : TextView = view.findViewById(R.id.tvName)
        val tvPosition : TextView = view.findViewById(R.id.tvPosition)
        val cvUser : CardView = view.findViewById(R.id.cvUser)
    }

}
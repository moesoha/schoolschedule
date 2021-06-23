package dev.soha.course202002.schedule.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.soha.course202002.schedule.android.R
import dev.soha.course202002.schedule.android.model.Lesson

class LessonCardListAdapter internal constructor(context: Context?, private val onclick: ((Lesson) -> Unit)? = null): RecyclerView.Adapter<LessonCardListAdapter.LessonViewHolder>() {
	inner class LessonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
		val container: CardView = itemView.findViewById(R.id.card)
		val name: TextView = itemView.findViewById(R.id.lesson_title)
		val time: TextView = itemView.findViewById(R.id.lesson_time)
		val place: TextView = itemView.findViewById(R.id.lesson_place)
	}

	private val inflater = LayoutInflater.from(context)

	var data = emptyList<Lesson>()
		set(value) {
			field = value
			this.notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
		val itemView = inflater.inflate(R.layout.listviewitem_lesson, parent, false)
		return LessonViewHolder(itemView)
	}

	override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
		val current = data[position]
		holder.name.text = current.name
		holder.time.text = current.sessionString
		holder.place.text = current.place
		current.color?.let { holder.container.setCardBackgroundColor(it) }
		this.onclick?.let { holder.itemView.setOnClickListener { it(current) } }
	}

	override fun getItemCount(): Int = data.size
}
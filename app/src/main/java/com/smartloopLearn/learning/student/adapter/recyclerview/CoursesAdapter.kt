import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.view.activities.coursedetails.CourseDetailsActivity

class CoursesAdapter(private val list: ArrayList<Courses>, private val context: Context) : RecyclerView.Adapter<CoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_frag_home_courses, parent, false)
        return CoursesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        val item = list[position]

        // Load image using Glide from URL
        Glide.with(context)
            .load(item.ImageURL)  // Assuming courseImage is a URL
            .placeholder(R.drawable.webdevelopmentslider)  // Placeholder image
            .error(R.drawable.instagram)  // Show this image if loading fails
            .into(holder.img)

        holder.name.text = item.CourseTitle
        holder.tutor.text = item.TeacherName
        holder.rating.rating = item.Rating.toFloat()

        // Handle click event for each course item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CourseDetailsActivity::class.java)
            intent.putExtra("COURSE_ID", item.CourseId) // Pass course ID or any data if needed
            context.startActivity(intent)
        }
    }
}

class CoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.ivCourseImage)
    val name: TextView = view.findViewById(R.id.tvCourseName)
    val tutor: TextView = view.findViewById(R.id.tvCourseTeacherName)
    val rating: RatingBar = view.findViewById(R.id.rbCourseRating)
}

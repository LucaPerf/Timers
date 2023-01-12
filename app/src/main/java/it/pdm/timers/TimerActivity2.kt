package it.pdm.timers

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import it.pdm.timers.fragments.AddTimerFragment
import it.pdm.timers.fragments.ProfileFragment
import it.pdm.timers.fragments.TimerFragment
import it.pdm.timers.fragments.TimerSalvatiFragment
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity2 : AppCompatActivity(), Communicator {
    private val timerFragment = TimerFragment()
    private val timerSalvatiFragment = TimerSalvatiFragment()
    private val profileFragment = ProfileFragment()

    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var eemail : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer2)

        val fragmentA = TimerFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentA).commit()

        val btn_navigation = findViewById<TextView>(R.id.bottom_navigation) as BottomNavigationView
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        auth = Firebase.auth

        //mostrare in alto a destra le impostazioni
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener{
            it.isCheckable = true
            when(it.itemId){
                R.id.profile -> replaceFragment(profileFragment)
                R.id.settings -> Toast.makeText(applicationContext, "Impostazioni", Toast.LENGTH_SHORT).show()
                R.id.logout -> logoutMenu(LoginActivity())
            }
            true
        }
        set_navheader()

        btn_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.timer -> replaceFragment(timerFragment)
                R.id.timer_salvati -> replaceFragment(timerSalvatiFragment)
            }
            true
        }
    }

    override fun passData(addItem: Int) {
        val bundle = Bundle()
        bundle.putInt("message", addItem)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentB = AddTimerFragment()
        fragmentB.arguments = bundle

        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.commit()
    }

    private fun set_navheader() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.nav_header, null)

        eemail = v.findViewById(R.id.mail)

        database = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Utenti")
            .child(Firebase.auth.currentUser!!.uid)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)!!
                    eemail.text = user.username
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TEST","Failed to read value")
            }
        })
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container, fragment)
        fragmentTransition.commit()
        drawerLayout.closeDrawers()
    }

    //usata aprire la navigation drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            set_navheader()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutMenu(activity: LoginActivity){
        val addDialog = AlertDialog.Builder(this)
        addDialog.setTitle("Logout")
        addDialog.setMessage("Sicuro di uscire?")
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d(ContentValues.TAG, "Logout")
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Log.d(ContentValues.TAG, "Cancel")
        }
        addDialog.create()
        addDialog.show()
    }
}
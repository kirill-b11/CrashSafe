package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class SavedModelsFragment : Fragment() {

    companion object {
        fun newInstance(): SavedModelsFragment {
            return SavedModelsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
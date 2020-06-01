package com.param.feedbackapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.param.feedbackapp.R
import com.param.feedbackapp.draw.DrawingActivity
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

private const val REQUEST_CODE_DRAW = 101
private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102
class FirstFragment : Fragment() {

    lateinit var adapter: DrawAdapter
    lateinit var rootView :View
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_first, container, false)
        checkPermissions()
        setFabClickEvent()
        return rootView
    }

    private fun setFabClickEvent() {
        rootView.fab_add.setOnClickListener {
            val intent = Intent(activity, DrawingActivity::class.java)
            startActivityForResult(intent,
                REQUEST_CODE_DRAW
            )
        }
    }

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }else{
            adapter = DrawAdapter(
                requireActivity().applicationContext,
                getFilesPath()
            )
            rv_feedback.adapter = adapter
        }
    }

    private fun getFilesPath(): ArrayList<String>{
        val resultList = ArrayList<String>()
        val imageDir = "${Environment.DIRECTORY_PICTURES}/Android Draw/"
        val path = Environment.getExternalStoragePublicDirectory(imageDir)
        path.mkdirs()
        val imageList = path.listFiles()
        for (imagePath in imageList){
            resultList.add(imagePath.absolutePath)
        }
        return resultList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
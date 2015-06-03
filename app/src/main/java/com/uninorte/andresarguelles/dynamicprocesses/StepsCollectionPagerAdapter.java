package com.uninorte.andresarguelles.dynamicprocesses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class StepsCollectionPagerAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    ArrayList<Step> steps;
    ArrayList<StepFragment> fragments;

    Context context;

    public StepsCollectionPagerAdapter (Context context,FragmentManager fm, ArrayList<Step> steps){
        super(fm);
        this.context = context;
        this.steps = steps;

        fragments = new ArrayList<StepFragment>();

        this.add(steps.get(0).step_id, 0);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		setPos(position);
        /*Step tmp = (Step)steps.get(position);
        return tmp.step_id+""; // CORREGIR ESTO. EL pos != del step_id
        */
        return "Step "+position;
	}
    public static void setPos(int pos) {
        StepsCollectionPagerAdapter.pos = pos;
    }
    public static int getPos(){
        return pos;
    }

    // Al anadir un nuevo fragment, le paso como parametro el step_id del paso al que corresponde, para luego desde el StepFragment, importar el elemento desde el Array de Steps.
    public void add(int step_id, int pos) {
        Bundle args = new Bundle();
        args.putInt("step_id", step_id);
        args.putInt("pos", pos);
        StepFragment stfg = (StepFragment) StepFragment.instantiate(context, StepFragment.class.getName(), args);
        fragments.add(stfg);
        this.notifyDataSetChanged();
    }


}

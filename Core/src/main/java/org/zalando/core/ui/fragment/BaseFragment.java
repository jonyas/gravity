package org.zalando.core.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import org.zalando.core.ui.presenter.BasePresenter;
import org.zalando.core.ui.view.BaseView;

/**
 * Base fragment to wrap all together some utility methods for fragments
 */
public abstract class BaseFragment extends Fragment implements BaseView {

  /**
   * Lifecycle method
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Force injection of dependencies
    injectDependencies();
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(getSubFragmentLayoutId(), container, false);
    // Inject fragment views
    ButterKnife.bind(this, fragmentView);
    // return inflated view
    return fragmentView;
  }

  /**
   * Lifecycle method
   */
  @SuppressWarnings("unchecked")
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {

    super.onActivityCreated(savedInstanceState);

    // Set the view to the presenter
    getPresenter().setView(this);
    // init objects
    final Bundle initBundle;
    if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
      initBundle = savedInstanceState;
    } else if (getArguments() != null) {
      initBundle = getArguments();
    } else {
      initBundle = new Bundle();
    }
    getPresenter().initialise(initBundle);
  }

  /**
   * Lifecycle method
   */
  @Override
  public void onResume() {

    super.onResume();
    if (getPresenter() != null) {
      getPresenter().resume();
    }
  }

  /**
   * Lifecycle method
   */
  @Override
  public void onDestroyView() {
    if (getPresenter() != null) {
      getPresenter().destroy();
    }
    super.onDestroyView();
  }

  /**
   * Lifecycle method
   *
   * @param outState {@link Bundle} where the state of the activity is stored
   */
  @Override
  public void onSaveInstanceState(Bundle outState) {

    if (getPresenter() != null) {
      getPresenter().onSaveInstanceState(outState);
    }
    super.onSaveInstanceState(outState);
  }

  /**
   * Called right before the fragment back stack is popped. Useful for preparing fragments for
   * transition animations.
   */
  public void onBackStackPop() {
  }

  /**
   * Provides the {@link Application} instance
   *
   * @return {@link Application} instance
   */
  protected final Application getApplication() {

    return (Application) getContext().getApplicationContext();
  }

  /**
   * Abstract method that provides which is the content view of the activity
   *
   * @return Layout content view resource id
   */
  @LayoutRes
  protected abstract int getSubFragmentLayoutId();

  /**
   * Provides the {@link BasePresenter} that controls the Activity
   *
   * @return {@link BasePresenter} responsible of controlling the Activity
   */
  @NonNull
  protected abstract BasePresenter getPresenter();

  /**
   * Force subclasses to inject dependencies accordingly
   */
  protected abstract void injectDependencies();

  /**
   * Returns whether or not the fragment is still visible
   *
   * @return If fragment is still visible
   */
  protected boolean isFragmentVisible() {

    return isAdded() && !getActivity().isFinishing();
  }
}

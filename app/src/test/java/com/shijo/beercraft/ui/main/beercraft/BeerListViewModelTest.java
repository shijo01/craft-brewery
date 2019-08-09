package com.shijo.beercraft.ui.main.beercraft;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import com.shijo.beercraft.data.db.BeerCraftDao;
import com.shijo.beercraft.data.db.CartDao;
import com.shijo.beercraft.data.network.BeerApi;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class BeerListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    BeerApi beerApi;
    @Mock
    BeerCraftDao beerCraftDao;
    @Mock
    CartDao cartDao;
    @Mock
    BeerListAdapter beerListAdapter;
    private BeerListViewModel viewModel;
    @Mock
    LifecycleOwner lifecycleOwner;
    Lifecycle lifecycle;
    @Mock
    Observer<Integer> observer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        viewModel = new BeerListViewModel(beerApi, beerCraftDao, cartDao, beerListAdapter);
        viewModel.getProgressBarVisibility().observeForever(observer);
    }

    @Test
    public void testNull() {
        when(beerApi.getBeers()).thenReturn(null);
        assertNotNull(viewModel.getProgressBarVisibility());
        assertTrue(viewModel.getProgressBarVisibility().hasObservers());
    }

}

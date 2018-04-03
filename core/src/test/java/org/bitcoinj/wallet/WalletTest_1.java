package org.bitcoinj.wallet;

import com.google.common.util.concurrent.Service;
import javafx.application.Platform;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Utils;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;

public class WalletTest_1 {

    @Test
    public void createWallet() throws IOException {
        NetworkParameters parameters = MainNetParams.get();
        WalletAppKit kit = new WalletAppKit(parameters,new File("."),"wallet-"+parameters.getPaymentProtocolId()){
            @Override
            protected void onSetupCompleted() {
                // Don't make the user wait for confirmations for now, as the intention is they're sending it
                // their own money!
                wallet().allowSpendingUnconfirmedTransactions();
            }
        };
        kit.setDownloadListener(new ProgressBarUpdater())
        .setBlockingStartup(false)
        .setUserAgent("wallet","1.0");

        if(kit.isChainFileLocked()){
            return;
        }
        kit.addListener(new Service.Listener() {
            @Override
            public void failed(Service.State from, Throwable failure) {
                System.out.println(failure.getMessage());
            }
        }, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        kit.startAsync();
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PeerGroup peer = kit.peerGroup();
        System.out.println("peer!!!");
//        Wallet wallet = new Wallet(parameters);
//
//        DeterministicSeed seed = wallet.getKeyChainSeed();
//        System.out.println("seed: " + seed.toString());
//        System.out.println("creation time: " + seed.getCreationTimeSeconds());
//        System.out.println("mnemonicCode: " + Utils.join(seed.getMnemonicCode()));
    }

    private class ProgressBarUpdater extends DownloadProgressTracker {
        @Override
        protected void progress(double pct, int blocksLeft, Date date) {
            super.progress(pct, blocksLeft, date);
        }

        @Override
        protected void doneDownload() {
            super.doneDownload();
        }
    }
}

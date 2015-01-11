use strict;
use warnings;
use Tie::File;

#my $OUTDIR='C:\ACTRun\D_ACT\Output';
my $inputDataSource = "IronGeekCloudInputData.csv";
my $outputDataResult = "ITGOPTIRONGEEKresult.csv";

open my $inputDataFile, $inputDataSource or die "Could not open $inputDataSource: $!\n";
#open my $outputDataFile, '>', $outputDataResult or die "Could not open file '$outputDataResult' $!\n";
tie my @outputDataFile, 'Tie::File', $outputDataResult or die "Could not write file '$outputDataResult' $!\n";

#print $outputDataFile "Symbol,Shares,Trade Price,Trade Date,Shares Outstanding,Open Price,Close Price,Avg Daily Volum,Side,Broker,Traded Value,Market Cap,Net Realized Gain vs. Open Price,Net Realized Gain vs. Close Price";
$outputDataFile[0] = "Symbol,Shares,Trade Price,Trade Date,Shares Outstanding,Open Price,Close Price,Avg Daily Volum,Side,Broker,Traded Value,Market Cap,Net Realized Gain vs. Open Price,Net Realized Gain vs. Close Price";
my $count = 0;

while( my $line = <$inputDataFile>)  {   
	$count++;
    next if $count == 1; 
	
	my @lineValues = split (',', $line);
	my $symbol = $lineValues[0];
	my $shares = $lineValues[1];
	my $tradePrice = $lineValues[2];
	my $tradeDate = $lineValues[3];
	my $sharesOutstanding = $lineValues[4];
	my $openPrice = $lineValues[5];
	my $closePrice = $lineValues[6];
	my $avgDailyVolum = $lineValues[7];
	my $side = $lineValues[8];
	chop($lineValues[9]); # remove newline char
	my $broker = $lineValues[9];
	
	my $tradedValue = $shares * $tradePrice;
	my $marketCap = $sharesOutstanding * $closePrice;
	my $netRealizedGainOpenPrice  = ($side eq "buy") ? ($openPrice - $tradePrice) * $shares : ($tradePrice - $openPrice) * $shares;
	my $netRealizedGainClosePrice = ($side eq "buy") ? ($closePrice - $tradePrice) * $shares : ($tradePrice - $closePrice) * $shares;
    #last if $. == 2;
	#last if ++$count == 2;
	
	#print $outputDataFile "$symbol,$shares,$tradePrice,$tradeDate,$sharesOutstanding,$openPrice,$closePrice,$avgDailyVolum,$side,$broker";
	#print $outputDataFile "$symbol,$shares,$tradePrice,$tradeDate,$sharesOutstanding,$openPrice,$closePrice,$avgDailyVolum,$side,$broker,$tradedValue,$marketCap,$netRealizedGainOpenPrice,$netRealizedGainClosePrice\\";
	$outputDataFile[$count] = "$symbol,$shares,$tradePrice,$tradeDate,$sharesOutstanding,$openPrice,$closePrice,$avgDailyVolum,$side,$broker,$tradedValue,$marketCap,$netRealizedGainOpenPrice,$netRealizedGainClosePrice";
}

close $inputDataFile;
#close $outputDataFile;
untie @outputDataFile;  
print "done\n";


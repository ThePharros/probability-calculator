# Probability Calculator

This plugin provides a tool for calculating various probabilities for statistical mechanics in OSRS. (Currently only calculates drop rates.)

## Drop Rates

### How to Use

1. Enter the probability of 1 successful trial (drop rate).
2. Enter the amount of total trials (kill count).
3. Enter the amount of successful trials (number of actual drops received).
4. Calculate and cry.

#### Configuration

You may configure how many decimal places the output will round up to in the configuration panel of the plugin. The default is set to 2 decimal places.

### The Math

Drop rates are calculated as **binomial distributions**:

<code>P(x) = <sub>n</sub>C<sub>x</sub> * p<sup>x</sup> * (1-p)<sup>n-x</sup></code>
<p>
where:<br>
<code>n</code> is the number of trials<br>
<code>x</code> is the number of successful trials<br>
<code>p</code> is the probability of one success<br>
<code><sub>n</sub>C<sub>x</sub></code> is "n choose x" which equals <code>n!/x!(n-x)!</code>
</p>

* To calculate the probability of getting **exactly** x successes would be to calculate 
<code>P(X=x)</code>.

* To calculate the probability of getting **at least** x successes would be to calculate 
<code>P(X>=x)</code>. This equates to <code>P(X=x) + P(X=x+1) + P(X=x+2) + ... + P(X=n)</code>.

* To calculate the probability of getting **zero** successes would be to calculate 
<code>P(X=0)</code>.

If we wish to calculate the probability of getting **at least 1 drop**, then we solve for <code>P(X>=1)</code>. Instead of solving for the sum of every probability at <code>x=1</code> and up, we can instead solve for its complement and subtract it from 1. Therefore, <code>P(X>=1) = 1 - P(X=0)</code>.

Solving for <code>P(X=0)</code> is quite simple, since <code><sub>n</sub>C<sub>x=0</sub></code> and <code>p<sup>x=0</sup></code> will always be equal to 1. Therefore, the binomial equation simplifies to <code>P(X=0) = (1-p)<sup>n</sup></code>. This is the same equation you would use to determine the chances of **getting no drops**. Simply subtracting this from 1 as previously mentioned will yield the probability of getting **at least 1 drop** in <code>n</code> kills.